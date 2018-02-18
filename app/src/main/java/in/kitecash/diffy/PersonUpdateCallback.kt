package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
  val personListWithDataChanges by lazy { mutableListOf<Person>() }
  val personListThatHasBeenDeletedFromRemote by lazy { mutableListOf<Person>() }

  val newlyPersonList by lazy { mutableListOf<Person>() }

  override fun onChanged(position: Int, count: Int, payload: Any?) {
    println("onChanged: $position $count")
    if (payload is Pair<*, *>) {
      personListWithDataChanges.add(payload.second as Person)
    }
  }

  override fun onInserted(position: Int, count: Int) {
    println("onInserted: $position $count")

    if (cachedPersonList.isEmpty()) {
      // The onInserted method is called for every single insertion. Using the element position (method params) can result into redundant data.
      newlyPersonList.addAll(remotePersonList.subList(position, position+count))
    }
  }

  override fun onRemoved(position: Int, count: Int) {
    personListThatHasBeenDeletedFromRemote.add(cachedPersonList.subList(position, position + count)[0])
  }

  override fun onMoved(fromPosition: Int, toPosition: Int) {
    println("onMoved: $fromPosition $toPosition")
  }
}
