package com.bd.examples.kafka.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Future

fun <V> Future<V>.subscribe(onCanceled: (() -> Unit)? = null, onComplete: (V) -> Unit): Future<V> {
    GlobalScope.launch {
        while (!isDone && !isCancelled) {
            delay(100)
        }
        if (isCancelled) onCanceled?.invoke()
        else onComplete(get())
    }
    return this
}
