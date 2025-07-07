package com.crocus.server.utils

import com.crocus.server.service.ElasSyncService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SyncInitializer(
    private val elasSyncService: ElasSyncService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        runCatching {
            scopeIO {
                print("Init ElasticSearch Data")
                syncElasticSearch()
            }
        }.onFailure {
            println("Error during sync initialization: ${it.message}")
            it.printStackTrace()
        }
    }

    private suspend fun syncElasticSearch() {
        elasSyncService.syncArticlesToEs()
        elasSyncService.syncBooksToEs()
        elasSyncService.syncAnnouncementsToEs()
        elasSyncService.syncQuestionsToEs()
    }

}