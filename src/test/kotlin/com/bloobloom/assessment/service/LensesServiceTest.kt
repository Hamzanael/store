package com.bloobloom.assessment.service

import com.bloobloom.assessment.constants.Constants
import com.bloobloom.assessment.constants.ConstantsTest.EMPTY_PRICE
import com.bloobloom.assessment.constants.ConstantsTest.NUMBER_TEN
import com.bloobloom.assessment.constants.ConstantsTest.TEST_LENSES
import com.bloobloom.assessment.constants.ConstantsTest.TEST_UPDATE_LENSES_REQUEST
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Currency.JOD
import com.bloobloom.assessment.model.Currency.USD
import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.repository.LensesRepository
import com.bloobloom.assessment.service.exceptions.lenses.NoLensesInStockAvailable
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Optional
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class LensesServiceTest {

    @MockK
    private lateinit var lensesRepository: LensesRepository

    @MockK
    private lateinit var currencyConverter: CurrencyConverter

    @InjectMockKs
    private lateinit var lensesService: LensesService

    @Test
    fun `should create lenses with the giving lenses object`() {
        every {
            lensesRepository.insert(
                TEST_LENSES
            )
        } answers {
            val insertedLenses = TEST_LENSES.copy()
            insertedLenses.id = "1"
            insertedLenses
        }
        val lenses = lensesService.create(
            TEST_LENSES
        )
        assertThat(lenses).isEqualTo(TEST_LENSES)
    }

    @Test
    fun `should get all lenses with currency conversion`() {
        every { lensesRepository.findAll() } returns mutableListOf(TEST_LENSES)
        every { currencyConverter.convert(any(), any()) } returns NUMBER_TEN
        val lenses = lensesService.getLenses()
        assertThat(lenses[0].prices.first().currency).isEqualTo(USD)
        assertThat(lenses).isEqualTo(listOf(TEST_LENSES))
    }

    @Test
    fun `should update lenses based on the request`() {
        val updatedLenses = TEST_LENSES.copy(
            description = "Updated Description",
            price = EMPTY_PRICE,
            stock = 10
        )
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES)
        every { lensesRepository.save(any()) } answers {
            updatedLenses.id = "1"
            updatedLenses
        }

        val lenses = lensesService.update("1", TEST_UPDATE_LENSES_REQUEST)
        assertThat(lenses).isEqualTo(updatedLenses)
    }

    @Test
    fun `should get lenses only if stock is greater than zero`() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES)
        val lenses = lensesService.getLensesIfAvailable("1")
        assertThat(lenses).isEqualTo(TEST_LENSES)
    }

    @Test
    fun `should return lenses with target currency`() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findAll() } returns mutableListOf(TEST_LENSES)
        every { currencyConverter.convert(any(), any()) } returns 20.0
        val lenses = lensesService.getLenses(JOD)
        assertThat(lenses[0].price.currency).isEqualTo(JOD)
        assertThat(lenses[0].price.value).isEqualTo(20.0)
    }

    @Test
    fun `should throw an exception if stock is less than zero`() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES.copy(stock = 0))
        Assertions.assertThatThrownBy { lensesService.getLensesIfAvailable("1") }
            .isInstanceOf(NoLensesInStockAvailable::class.java)
    }


    @Test
    fun `should be able to buy lenses if there is items in the stock and the quantity is the default quantity`() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES)
        every { lensesRepository.save(any()) } answers {
            TEST_LENSES.copy(stock = TEST_LENSES.stock - Constants.NUMBER_OF_ALLOWED_QUANTITY)
        }
        val lenses = lensesService.buyLenses("1", Constants.NUMBER_OF_ALLOWED_QUANTITY)
        assertThat(lenses.stock).isEqualTo(TEST_LENSES.stock - Constants.NUMBER_OF_ALLOWED_QUANTITY)
    }

    @Test
    fun `should not be able to buy lenses if there is no items in the stock`() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES.copy(stock = 0))
        Assertions.assertThatThrownBy { lensesService.buyLenses("1", Constants.NUMBER_OF_ALLOWED_QUANTITY) }
            .isInstanceOf(
                NoLensesInStockAvailable::class.java
            )
    }

    @Test
    fun addLensesToStock() {
        TEST_LENSES.id = "1"
        every { lensesRepository.findById(any()) } returns Optional.of(TEST_LENSES)
        every { lensesRepository.save(any()) } answers {
            TEST_LENSES.copy(stock = TEST_LENSES.stock + Constants.NUMBER_OF_ALLOWED_QUANTITY)
        }
        val lenses = lensesService.addLensesToStock("1", Constants.NUMBER_OF_ALLOWED_QUANTITY)
        assertThat(lenses.stock).isEqualTo(TEST_LENSES.stock + Constants.NUMBER_OF_ALLOWED_QUANTITY)
    }

    @Test
    fun delete() {
        TEST_LENSES.id = "1"
        every { lensesRepository.deleteById(any()) } answers {}
        lensesService.delete("1")
        verify(atLeast = 1) { lensesRepository.deleteById(any()) }
    }


}