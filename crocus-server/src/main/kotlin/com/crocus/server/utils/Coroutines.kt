package com.crocus.server.utils

import kotlinx.coroutines.*

// --------------------------------------------- withContext -------------------------------------------------
/**
 * @return withContext Dispatchers.Default 的协程上下文
 */
suspend fun <T> contentDefault(block: suspend () -> T): T = withContext(Dispatchers.Default) { block() }

/**
 * @return withContext Dispatchers.IO 的协程上下文
 */
suspend fun <T> contentIO(block: suspend () -> T): T = withContext(Dispatchers.IO) { block() }

/**
 * @return withContext Dispatchers.Main 的协程上下文
 */
suspend fun <T> contentMain(block: suspend () -> T): T = withContext(Dispatchers.Main) { block() }



// --------------------------------------------- CoroutineScope ------------------------------------------------−
/**
 * @return CoroutineScope Dispatchers.Default 的  Deferred<T>
 */
fun <T> scopeDefault(block: suspend () -> T):  Deferred<T> = CoroutineScope(Dispatchers.Default).async { block() }


/**
 * @return CoroutineScope Dispatchers.IO 的 Deferred<T>
 */
fun <T> scopeIO(block: suspend () -> T): Deferred<T> = CoroutineScope(Dispatchers.IO).async { block() }

/**
 * @return CoroutineScope Dispatchers.Main 的 Deferred<T>
 */
fun <T> scopeMain(block: suspend () -> T): Deferred<T> = CoroutineScope(Dispatchers.Main).async { block() }






