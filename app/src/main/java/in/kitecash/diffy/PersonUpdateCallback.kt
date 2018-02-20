package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
  private val copyOfCachedPersonsList = cachedPersonList.toMutableList()

  val newlyInsertedPersons by lazy { mutableListOf<Person>() }
  val deletedPersons       by lazy { mutableListOf<Person>() }
  val updatedCacheList by lazy { mutableListOf<Person>() }
  val updatedRemoteList by lazy { mutableListOf<Person>() }

  override fun onChanged(position: Int, count: Int, payload: Any?) {
    println("onChanged: Position: $position Count: $count")

    // Android's DiffUtil returns a payload of what has changed from the either of the two lists.
    if (payload is Pair<*, *>) {
      updatedCacheList.add(payload.first as Person)
      updatedRemoteList.add(payload.second as Person)
    } else  {
      throw UnsupportedOperationException("Cannot be handled")
    }
  }

  override fun onInserted(position: Int, count: Int) {
    println("onInserted: Position: $position Count: $count")

    if (cachedPersonList.isEmpty() || copyOfCachedPersonsList.isEmpty()) {
      val insertedList = remotePersonList.subList(position, position + count)
      newlyInsertedPersons.addAll(insertedList)
      copyOfCachedPersonsList.addAll(insertedList)
    } else {
      // Find the ID in the existing cacheList after which the items from the remote list can be inserted.
      val pivotId = copyOfCachedPersonsList[0].id
      val pivotIndex = remotePersonList.indexOfFirst { it.id == pivotId }
      val startIndex = if (position > 0) pivotIndex + position else pivotIndex - count
      val endIndex = startIndex + count
      val insertedList = remotePersonList.subList(startIndex, endIndex)
      newlyInsertedPersons.addAll(insertedList)
      copyOfCachedPersonsList.addAll(position, insertedList)
    }
  }

  override fun onRemoved(position: Int, count: Int) {
    println("onRemoved: Position: $position Count: $count")

    deletedPersons.addAll(copyOfCachedPersonsList.subList(position, position + count))

    var lastIndex = position + count - 1
    var startIndex = position

    // Remove the item from the position if the count is 1.
    if (count == 1) {
      copyOfCachedPersonsList.removeAt(position)
    } else {
      // Else, delete the `count` number of items after the specified position
      // TODO(ak) - Check the complexity of the logic and find a better solution which is faster.
      while (lastIndex >= position && startIndex <= position + count) {
        copyOfCachedPersonsList.removeAt(position)
        lastIndex--
        startIndex++
      }
    }
  }

  override fun onMoved(fromPosition: Int, toPosition: Int) {
    println("onMoved: From: $fromPosition To: $toPosition")

    val person = copyOfCachedPersonsList.get(fromPosition)
    copyOfCachedPersonsList.remove(person)
    copyOfCachedPersonsList.add(toPosition, person)
  }
}
