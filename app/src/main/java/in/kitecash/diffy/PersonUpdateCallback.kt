package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
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
    println(remotePersonList[position])
    newlyInsertedPersons.addAll(remotePersonList.subList(position, position +  count - 1))
  }

  override fun onRemoved(position: Int, count: Int) {
    println("onRemoved: Position: $position Count: $count")

    deletedPersons.addAll(cachedPersonList.subList(position, position +  count))
  }

  override fun onMoved(fromPosition: Int, toPosition: Int) {
    println("onMoved: $fromPosition $toPosition")
  }
}
