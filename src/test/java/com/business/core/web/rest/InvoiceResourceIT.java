package com.business.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.business.core.IntegrationTest;
import com.business.core.domain.Invoice;
import com.business.core.repository.InvoiceRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
//@IntegrationTest
@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_TOTAL_AMOUNT = 0D;
    private static final Double UPDATED_TOTAL_AMOUNT = 1D;

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity() {
        Invoice invoice = new Invoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity() {
        Invoice invoice = new Invoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoiceRepository.deleteAll();
        invoice = createEntity();
    }

    //    @Test
    //    void createInvoice() throws Exception {
    //        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
    //        // Create the Invoice
    //        restInvoiceMockMvc
    //            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
    //            .andExpect(status().isCreated());
    //
    //        // Validate the Invoice in the database
    //        List<Invoice> invoiceList = invoiceRepository.findAll();
    //        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
    //        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
    //        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    //        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
    //        assertThat(testInvoice.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    //        assertThat(testInvoice.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    //    }

    @Test
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId("existing_id");

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceNumber(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setIssueDate(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDueDate(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTotalAmount(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    //    @Test
    //    void getAllInvoices() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        // Get all the invoiceList
    //        restInvoiceMockMvc
    //            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId())))
    //            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
    //            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
    //            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
    //            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())));
    //    }

    //    @Test
    //    void getInvoice() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        // Get the invoice
    //        restInvoiceMockMvc
    //            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
    //            .andExpect(status().isOk())
    //            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    //            .andExpect(jsonPath("$.id").value(invoice.getId()))
    //            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
    //            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
    //            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
    //            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()));
    //    }

    @Test
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    //    @Test
    //    void putExistingInvoice() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
    //
    //        // Update the invoice
    //        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();
    //        updatedInvoice
    //            .invoiceNumber(UPDATED_INVOICE_NUMBER)
    //            .issueDate(UPDATED_ISSUE_DATE)
    //            .dueDate(UPDATED_DUE_DATE)
    //            .totalAmount(UPDATED_TOTAL_AMOUNT);
    //
    //        restInvoiceMockMvc
    //            .perform(
    //                put(ENTITY_API_URL_ID, updatedInvoice.getId())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoice))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Invoice in the database
    //        List<Invoice> invoiceList = invoiceRepository.findAll();
    //        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    //        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
    //        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    //        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
    //        assertThat(testInvoice.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    //        assertThat(testInvoice.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    //    }

    @Test
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    //    @Test
    //    void partialUpdateInvoiceWithPatch() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
    //
    //        // Update the invoice using partial update
    //        Invoice partialUpdatedInvoice = new Invoice();
    //        partialUpdatedInvoice.setId(invoice.getId());
    //
    //        partialUpdatedInvoice.totalAmount(UPDATED_TOTAL_AMOUNT);
    //
    //        restInvoiceMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Invoice in the database
    //        List<Invoice> invoiceList = invoiceRepository.findAll();
    //        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    //        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
    //        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    //        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
    //        assertThat(testInvoice.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    //        assertThat(testInvoice.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    //    }

    //    @Test
    //    void fullUpdateInvoiceWithPatch() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
    //
    //        // Update the invoice using partial update
    //        Invoice partialUpdatedInvoice = new Invoice();
    //        partialUpdatedInvoice.setId(invoice.getId());
    //
    //        partialUpdatedInvoice
    //            .invoiceNumber(UPDATED_INVOICE_NUMBER)
    //            .issueDate(UPDATED_ISSUE_DATE)
    //            .dueDate(UPDATED_DUE_DATE)
    //            .totalAmount(UPDATED_TOTAL_AMOUNT);
    //
    //        restInvoiceMockMvc
    //            .perform(
    //                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
    //                    .contentType("application/merge-patch+json")
    //                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
    //            )
    //            .andExpect(status().isOk());
    //
    //        // Validate the Invoice in the database
    //        List<Invoice> invoiceList = invoiceRepository.findAll();
    //        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    //        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
    //        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    //        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
    //        assertThat(testInvoice.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    //        assertThat(testInvoice.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    //    }

    @Test
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }
    //    @Test
    //    void deleteInvoice() throws Exception {
    //        // Initialize the database
    //        invoiceRepository.save(invoice);
    //
    //        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();
    //
    //        // Delete the invoice
    //        restInvoiceMockMvc
    //            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
    //            .andExpect(status().isNoContent());
    //
    //        // Validate the database contains one less item
    //        List<Invoice> invoiceList = invoiceRepository.findAll();
    //        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    //    }
}
