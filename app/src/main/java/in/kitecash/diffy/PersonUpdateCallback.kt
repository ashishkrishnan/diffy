package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
  val duplicateCachedPersonsList = cachedPersonList.toMutableList()

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
      println(payload)
      throw UnsupportedOperationException("Cannot be handled")
    }
  }

  override fun onInserted(position: Int, count: Int) {
    println("onInserted: Position: $position Count: $count")

    if (cachedPersonList.isEmpty()) {
      val insertedList = remotePersonList.subList(position, position + count)
      newlyInsertedPersons.addAll(insertedList)
      duplicateCachedPersonsList.addAll(insertedList)
    } else {
      println("$duplicateCachedPersonsList")
      val pivotId = duplicateCachedPersonsList[0].id
      val pivotIndex = remotePersonList.indexOfFirst { it.id == pivotId }
      val startIndex = if (position > 0) pivotIndex + position else pivotIndex - count
      val endIndex = startIndex + count
      val insertedList = remotePersonList.subList(startIndex, endIndex)
      newlyInsertedPersons.addAll(insertedList)
      duplicateCachedPersonsList.addAll(position, insertedList)
    }

//    val remotePosition = remotePersonList.count() - count // FIXME: Wrong code
//    val insertedList = remotePersonList.subList(remotePosition, remotePosition + count)
//    newlyInsertedPersons.addAll(insertedList)
//    duplicateCachedPersonsList.addAll(position, insertedList)
  }

  override fun onRemoved(position: Int, count: Int) {
    println("onRemoved: Position: $position Count: $count")

    deletedPersons.addAll(duplicateCachedPersonsList.subList(position, position + count))
    for (pos in IntRange(position, position + count - 1)) {
      duplicateCachedPersonsList.removeAt(pos)
    }
  }

  override fun onMoved(fromPosition: Int, toPosition: Int) {
    println("onMoved: From: $fromPosition To: $toPosition")

    val person = duplicateCachedPersonsList.get(fromPosition)
    duplicateCachedPersonsList.remove(person)
    duplicateCachedPersonsList.add(toPosition, person)
  }
}
