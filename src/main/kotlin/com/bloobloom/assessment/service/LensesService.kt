package com.bloobloom.assessment.service

import com.bloobloom.assessment.api.v1.request.UpdateLensesRequest
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.Lenses
import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.repository.LensesRepository
import com.bloobloom.assessment.service.exceptions.lenses.NoLensesInStockAvailable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LensesService {
    @Autowired
    private lateinit var lensesRepository: LensesRepository

    @Autowired
    private lateinit var currencyConverter: CurrencyConverter

    fun create(lenses: Lenses): Lenses = lensesRepository.insert(lenses)

    fun getLenses(): List<Lenses> {
        val allLenses = lensesRepository.findAll()
        calculateLensesPricesInMultiCurrency(allLenses)
        return allLenses
    }

    fun getLenses(currency: Currency): List<Lenses> {
        val allLenses = lensesRepository.findAll()
        calculateLensesPricesInMultiCurrency(allLenses)
        allLenses.forEach {
            it.price = Price(currencyConverter.convert(it.price.value, currency), currency)
        }
        return allLenses
    }

    fun getLensesIfAvailable(id: String): Lenses {
        val lenses = lensesRepository.findById(id).orElseThrow { Exception("Frame not found") }
        if (lenses.stock == 0) {
            throw NoLensesInStockAvailable("lenses with id $id is not in stock")
        }
        return lenses
    }

    fun update(id: String, frameRequest: UpdateLensesRequest): Lenses {
        val lenses = lensesRepository.findById(id).orElseThrow { Exception("Lenses not found") }
        val newLenses = lenses.copy(
            color = frameRequest.color ?: lenses.color,
            description = frameRequest.description ?: lenses.description,
            prescriptionType = frameRequest.prescriptionType ?: lenses.prescriptionType,
            lensesType = frameRequest.lensesType ?: lenses.lensesType,
            stock = frameRequest.stock ?: lenses.stock,
            price = frameRequest.price ?: lenses.price
        )
        newLenses.id = lenses.id
        return lensesRepository.save(newLenses)
    }

    fun buyLenses(id: String, quantity: Int): Lenses {
        val lenses = lensesRepository.findById(id).orElseThrow { Exception("Lenses not found") }
        if (lenses.stock < quantity) {
            throw NoLensesInStockAvailable("Not enough stock")
        }
        val newLenses = lenses.copy(stock = lenses.stock - quantity)
        newLenses.id = lenses.id
        return lensesRepository.save(newLenses)
    }

    fun addLensesToStock(id: String, quantity: Int): Lenses {
        logger.info("Adding $quantity lenses with $id to stock")
        val lenses = lensesRepository.findById(id).orElseThrow { Exception("Lenses not found") }
        val newLenses = lenses.copy(stock = lenses.stock + quantity)
        newLenses.id = lenses.id
        return lensesRepository.save(newLenses)
    }

    fun delete(id: String) = lensesRepository.deleteById(id)

    private fun calculateLensesPricesInMultiCurrency(allLenses: List<Lenses>) {
        allLenses.forEach { lenses ->
            lenses.prices = Currency.values().map { currency ->
                Price(
                    currency = currency,
                    value = currencyConverter.convert(lenses.price.value, currency)
                )
            }.toSet()
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LensesService::class.java)
    }
}
