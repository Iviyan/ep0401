package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Test
import com.iv.ep0401k.models.TestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TestController {

    @Autowired
    lateinit var repository: TestRepository

    @GetMapping("/")
    fun Index(model: Model) : String {
        val testRecords = repository.findAll()

        model.addAttribute("records", testRecords)

        return "Test"
    }

    @PostMapping("/")
    fun Add(@RequestParam text: String) : String {
        val testRecords = repository.save(Test(0, ""))

        return "reditect:/test"
    }

}