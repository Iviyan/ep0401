package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import kotlin.jvm.optionals.getOrNull

@Controller
class ClientsController {

    @Autowired
    lateinit var clientsRepository: ClientsRepository

    @Autowired
    lateinit var passportsRepository: PassportsRepository

    @Autowired
    lateinit var booksRepository: BooksRepository

    @GetMapping("/clients")
    fun Index(model: Model): Any {
        val clients = clientsRepository.findAll()
        val clientsDto = clients.map { ClientDto.from(it) }

        model.addAttribute("clients", clientsDto)

        return "clients/Clients"
    }

    /*@GetMapping("/clients/json")
    @ResponseBody
    fun getJson(): Any {
        val clients = clientsRepository.findAll()
        return clients.map { ClientDto.from(it) }
    }*/

    @GetMapping("/books/{id}/clients")
    @ResponseBody
    fun getBookClients(
        @PathVariable(name = "id") id: Int,
    ): Any {
        val clients = booksRepository.findById(id).orElseThrow().clients
        return clients!!.map { ClientDto.from(it) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/clients/{id}")
    fun getClient(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val client = clientsRepository.findById(id)
        val clientDto = if (client.isPresent) Optional.of(ClientDto.from(client.get())) else Optional.empty()
        model.addAttribute("model", clientDto)
        model.addAttribute("client", clientDto.getOrNull())
        if (client.isEmpty) response.status = HttpStatus.NOT_FOUND.value()
        return "clients/Client"
    }


    @PostMapping("/clients/{id}")
    fun editClient(
        @PathVariable(name = "id") id: Int,
        @Valid @ModelAttribute("client") client: ClientDto, bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(client))
            return "clients/Client"
        }

        val clientData = clientsRepository.findById(id).orElseThrow()
        val currentPassport = clientData.passport.passportId

        client.id = id
        val clientEntity = client.toClient()
        if (currentPassport.serial != client.passportSerial || currentPassport.number != client.passportNumber) {
            passportsRepository.updatePassport(
                currentPassport.serial, currentPassport.number,
                client.passportSerial, client.passportSerial
            )
        }
        passportsRepository.save(clientEntity.passport)
        clientsRepository.save(clientEntity)

        return "redirect:/clients"
    }

    @GetMapping("/clients/new")
    fun addClient(client: ClientDto, model: Model): String {
        model.addAttribute("client", client)
        return "clients/NewClient"
    }

    @PostMapping("/clients")
    fun addClient(@Valid @ModelAttribute("client") client: ClientDto, bindingResult: BindingResult,
                  model: Model): String {
        if (bindingResult.hasErrors()) return "clients/NewClient"

        val clientEntity = client.toClient()

        passportsRepository.save(clientEntity.passport)
        clientsRepository.save(clientEntity)

        return "redirect:/clients"
    }

    @GetMapping("/clients/{id}/delete")
    fun deleteClient(@PathVariable(name = "id") id: Int): String {
        clientsRepository.deleteById(id)
        return "redirect:/clients"
    }
}