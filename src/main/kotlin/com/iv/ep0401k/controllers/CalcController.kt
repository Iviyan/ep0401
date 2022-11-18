package com.iv.ep0401k.controllers

import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

enum class CalcOperators { ADD, SUB, MUL, DIV }

@Controller
class CalcController {

    fun doCalc(a: Double, b: Double, op: CalcOperators) : Double = when (op) {
        CalcOperators.ADD -> a + b
        CalcOperators.SUB -> a - b
        CalcOperators.MUL -> a * b
        CalcOperators.DIV -> a / b
    }

    @GetMapping("/")
    fun Index(model: Model, @RequestParam a: Double?, @RequestParam b: Double?, @RequestParam op: CalcOperators?) : String {
        model.addAttribute("op", op ?: CalcOperators.ADD)
        model.addAttribute("a", a ?: 0)
        model.addAttribute("b", b ?: 0)

        if (a == null || b == null || op == null) return "Calc"

        val result = doCalc(a, b, op)
        model.addAttribute("result", result)

        return "Calc"
    }

    @GetMapping("/api") @ResponseBody
    fun Calc(@RequestParam a: Double?, @RequestParam b: Double?, @RequestParam op: CalcOperators?) : ResponseEntity<String> {
        if (a == null || b == null || op == null)
            return  ResponseEntity<String>("Bad params", HttpStatus.BAD_REQUEST)

        val result = doCalc(a, b, op)

        return ResponseEntity(result.toString(), HttpStatus.OK)
    }

}

class StringToEnumConverter : Converter<String, CalcOperators> {
    override fun convert(source: String): CalcOperators =
        CalcOperators.valueOf(source.uppercase(Locale.getDefault()))
}