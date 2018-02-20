package `in`.kitecash.diffy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import khronos.beginningOfDay
import khronos.days

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val personA = Person(1L, "Ashish", 22, "MALE", "+006", updated = 2.days.ago.beginningOfDay)
    val personB = Person(2L, "Afnan", 22, "FEMALE", "+007", updated = 3.days.ago.beginningOfDay)
    val personC = Person(3L, "Gurpreet", 22, "MALE", "+008", updated = 4.days.ago.beginningOfDay)
    val personD = Person(4L, "Dinesh", 22, "MALE", "+008", updated = 4.days.ago.beginningOfDay)
    val personE = Person(5L, "Adam", 22, "MALE", "+008", updated = 4.days.ago.beginningOfDay)

    val cachedPersonsList = listOf(personD, personB, personE, personC, personA)

    val remotePersonsList = listOf(
        personE,
        personB,
        personA.copy(name = "Bhawna", updated = 1.days.ago.beginningOfDay),
        personC
    )

    // Calculate Diff
    val personDiffUtilCallback = PersonDiffUtilCallback(cachedPersonsList, remotePersonsList)
    val result = DiffUtil.calculateDiff(personDiffUtilCallback)

    // Find Changes in the list by above calculated diff.
    val personUpdateCallback = PersonUpdateCallback(cachedPersonsList, remotePersonsList)
    result.dispatchUpdatesTo(personUpdateCallback)

    println("Inserted")
    personUpdateCallback.newlyInsertedPersons.forEach { println(it) }

    println("Deleted")
    personUpdateCallback.deletedPersons.forEach { println(it) }

    println("Updated Old List")
    personUpdateCallback.updatedCacheList.forEach { println(it) }

    println("Updated New List")
    personUpdateCallback.updatedRemoteList.forEach { println(it) }
  }
}
