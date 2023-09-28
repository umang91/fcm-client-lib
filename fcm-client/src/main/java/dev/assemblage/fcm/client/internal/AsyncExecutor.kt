package dev.assemblage.fcm.client.internal

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

internal object AsyncExecutor {
    private var executor: ExecutorService = Executors.newCachedThreadPool()

    fun <T> submit(task: () -> T): Future<T> {
        return executor.submit(task)
    }
}
