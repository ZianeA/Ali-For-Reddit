package util.domain

import io.reactivex.functions.Predicate

fun <T> match(matcher: (T) -> Unit): Predicate<T> {
    return Predicate {
        matcher.invoke(it)
        true
    }
}