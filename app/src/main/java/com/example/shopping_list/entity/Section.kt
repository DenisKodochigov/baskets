package com.example.shopping_list.entity

interface Section: Comparable<Section>  {
    var idSection: Long
    var nameSection: String
    var colorSection: String
}
