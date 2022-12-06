package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "shipments")
class Shipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @ManyToOne
    @JoinColumn(name="supplier_id", nullable=false)
    @field:NotNull
    var supplier: Supplier? = null,

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    @field:NotNull
    var book: Book? = null,

    @Column(name = "datetime")
    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var datetime: LocalDateTime = LocalDateTime.now(),
) : ModelBase<Int>

@Repository
interface ShipmentsRepository : CrudRepository<Shipment, Int>