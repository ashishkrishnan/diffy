package `in`.kitecash.diffy

import android.support.v7.util.DiffUtil

class PersonDiffUtilCallback(
    val cachedPersons: List<Person>,
    val remotePersons: List<Person>
): DiffUtil.Callback() {
  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val cachedPersonId = cachedPersons[oldItemPosition].id
    val remotePersonId = remotePersons[newItemPosition].id
    return cachedPersonId == remotePersonId
  }

  override fun getOldListSize(): Int {
    return cachedPersons.size
  }

  override fun getNewListSize(): Int {
    return remotePersons.size
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    val cachedUpdated = cachedPersons[oldItemPosition].updated
    val remoteUpdated = remotePersons[newItemPosition].updated
    return cachedUpdated == remoteUpdated
  }

  override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Pair<Person, Person> {
    val previouslyUpdatedPerson = cachedPersons[oldItemPosition]
    val newlyUpdatedPerson = remotePersons[newItemPosition]

    return Pair(previouslyUpdatedPerson, newlyUpdatedPerson)
  }
}