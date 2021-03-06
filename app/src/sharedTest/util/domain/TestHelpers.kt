package util.domain

import android.content.Context
import androidx.core.content.contentValuesOf
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.domain.feed.SortType
import io.reactivex.functions.Predicate
import io.reactivex.observers.TestObserver

fun <T> match(matcher: (T) -> Unit): Predicate<T> {
    return Predicate { matcher.invoke(it); true }
}

fun <T> TestObserver<T>.assertLastValue(predicate: (T) -> Unit) {
    assertValueAt(valueCount() - 1) { predicate.invoke(it); true }
}

fun <T> TestObserver<T>.assertFirstValue(predicate: (T) -> Unit) {
    assertValueAt(1) { predicate.invoke(it); true }
}

fun createDatabase(): RedditDatabase {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
        .allowMainThreadQueries()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                seedDatabase(db)
            }
        })
        .build()
}

private fun seedDatabase(db: SupportSQLiteDatabase) {
    SortType.values().forEach {
        db.insert(
            "SortTypeEntity",
            OnConflictStrategy.IGNORE,
            contentValuesOf("name" to it.name)
        )
    }
}
