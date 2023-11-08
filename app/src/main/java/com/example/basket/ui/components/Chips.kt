package com.example.basket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun ChipsUnit(
    listUnit: List<UnitApp>,
    edit: Boolean,
    onClick: (UnitApp) -> Unit,
    unitArticle: UnitApp?)
{
    var selectedUnit by remember { mutableStateOf<UnitApp>(UnitDB())}
    if (unitArticle != null) selectedUnit = unitArticle

    Box{
        Text(text = stringResource(id = R.string.units),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 12.dp))
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        ) {

            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                listUnit.forEach { unit ->
                    InputChip(
                        modifier = Modifier.padding(start = 4.dp),
                        selected = selectedChipsUnit(unitArticle, unit, selectedUnit, edit),
                        label = { Text(text = unit.nameUnit) },
                        onClick = {
                            if (unitArticle == null){
                                selectedUnit = unit
                                onClick(selectedUnit)
                            } else {
                                unitArticle
                            }
                        },
                    )
                }
            }
        }
    }
 }

@Composable fun selectedChipsUnit(
    unitArticle: UnitApp?, unit: UnitApp, selectedUnit: UnitApp, enable: Boolean
):Boolean {
    return if (unitArticle == null) {
        unit == selectedUnit
    } else {
        if (enable) unit == selectedUnit
        else unit == unitArticle
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun ChipsSections(
    edit: Boolean,
    listSection: List<Section>,
    sectionArticle: Section?,
    onClick: (Section) -> Unit) {

    var selectedSection by remember { mutableStateOf<Section>(SectionDB())}
    Box {
        Text(
            text = stringResource(id = R.string.sections),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        ) {

            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                listSection.forEach { section ->
                    InputChip(
                        modifier = Modifier.padding(start = 4.dp),
                        selected = selectedChipsSection(sectionArticle, section, selectedSection),
                        label = { Text(text = section.nameSection) },
                        onClick = {
                            if (sectionArticle == null){
                                selectedSection = section
                                onClick(selectedSection)
                            } else {
                                sectionArticle
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable fun selectedChipsSection(
    sectionArticle: Section?, section: Section, selectedSection: Section
) = if (sectionArticle == null) section == selectedSection else section == sectionArticle
