package com.laner.conectadeporte.src

// Clase Enum para los rangos de edad de los apuntados
// Se define a cada string con los numeros de las edades, y delante el identificador del enum
enum class Rango_Edad(val stringValue: String) {
    MENOR_DE_10("<10"),
    DE_10_A_15("10-15"),
    DE_15_A_20("15-20"),
    DE_20_A_25("20-25"),
    DE_25_A_30("25-30"),
    MAYOR_DE_30(">30")
}