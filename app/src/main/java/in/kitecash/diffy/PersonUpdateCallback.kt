package `in`.kitecash.diffy

import android.support.v7.util.ListUpdateCallback

class PersonUpdateCallback(
    private val cachedPersonList: List<Person>,
    private val remotePersonList: List<Person>
): ListUpdateCallback {
  val newPersonList = mutableListOf<Person>()
  val updatedPersonList = mutableListOf<Person>()

  override fun onChanged(position: Int, count: Int, payload: Any?) {
    TODO("not implemented") // Not implemented
  }

  override fun onInserted(position: Int, count: Int) {
    if (cachedPersonList.isEmpty()) {
      newPersonList.addAll(remotePersonList)
    }
  }

  override fun onRemoved(position: Int, count: Int) {
    val drop = cachedPersonList.drop(position)
  }

  override fun onMoved(fromPosition: Int, toPosition: Int) {}
}
