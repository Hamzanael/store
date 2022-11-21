package com.bloobloom.assessment.init

import com.bloobloom.assessment.integration.currency.CurrencyConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class StartupApplicationInitializer : ApplicationListener<ContextRefreshedEvent?> {
    @Autowired
    private lateinit var currencyConverter: CurrencyConverter

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        loadLatestCurrencyRates()
    }

    private fun loadLatestCurrencyRates() {
        logger.info("Loading latest currency rates")
//        currencyConverter.getCurrencyRates()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(StartupApplicationInitializer::class.java)
    }
}