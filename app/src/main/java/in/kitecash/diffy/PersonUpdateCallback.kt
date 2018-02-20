package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
  val copyOfCachedPersonsList = cachedPersonList.toMutableList()

  val newlyInsertedPersons by lazy { mutableListOf<Person>() }
  val deletedPersons       by lazy { mutableListOf<Person>() }
  val updatedOldList by lazy { mutableListOf<Person>() }
  val updatedNewList by lazy { mutableListOf<Person>() }

  override fun onChanged(position: Int, count: Int, payload: Any?) {
    println("onChanged: Position: $position Count: $count")

    if (payload is Pair<*, *>) {
      updatedOldList.add(payload.first as Person)
      updatedNewList.add(payload.second as Person)
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

    if (count == 1) {
      copyOfCachedPersonsList.removeAt(position)
    } else {
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
