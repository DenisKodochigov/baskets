package com.example.shopping_list.entity

import com.example.shopping_list.entity.interfaces.UnitInterface

data class UnitApp (
    override val idUnit: Long = 0,
    override val nameUnit: String = "",
    override var isSelected: Boolean = false
): UnitInterface{
    fun mapping(item:UnitInterface): UnitApp{
        return UnitApp(
            idUnit = item.idUnit,
            nameUnit = item.nameUnit,
            isSelected = item.isSelected
        )
    }
}