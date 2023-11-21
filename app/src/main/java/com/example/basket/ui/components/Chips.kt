package com.example.basket.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.TypeText
import com.example.basket.entity.UnitApp
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.styleApp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun ChipsUnit(
    edit: Boolean = true,
    listUnit: List<UnitApp>,
    unitArticle: UnitApp?,
    onClick: (UnitApp) -> Unit, )
{
    val selectedUnit = if (unitArticle != null) remember { mutableStateOf(unitArticle)}
                        else remember { mutableStateOf(UnitDB() as UnitApp)}

    Column{
        TextApp(text = stringResource(id = R.string.units),
            style = styleApp(TypeText.NAME_SLIDER),
            modifier = Modifier.padding(start = 12.dp))
        Box(modifier = Modifier.padding(top = 0.dp).border(width = 1.dp,
                    color = colorApp.primary, shape = shapesApp.small)
        ){
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                listUnit.forEach { unit ->
                    InputChip(
                        modifier = Modifier.padding(start = 4.dp),
                        selected = unit == selectedUnit.value,
                        label = {
                            TextApp(text = unit.nameUnit, textAlign = TextAlign.Center,
                                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)) },
                        onClick = {
                            if (edit){
                                selectedUnit.value = unit
                                onClick(selectedUnit.value)
                            }
                        },
                    )
                }
            }
        }
    }
 }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun ChipsSections(
    edit: Boolean = true,
    listSection: List<Section>,
    sectionArticle: Section?,
    onClick: (Section) -> Unit)
{
    val lazyState = rememberLazyStaggeredGridState()
    val selectedSection = if (sectionArticle == null) remember { mutableStateOf(SectionDB() as Section)}
                            else remember { mutableStateOf(sectionArticle)}
    Column {
        TextApp(
            text = stringResource(id = R.string.sections),
            style = styleApp(TypeText.NAME_SLIDER),
            modifier = Modifier.padding(start = 12.dp)
        )
        Box(modifier = Modifier)
        {
            Divider(color = colorApp.primary, thickness = 1.dp)
            LazyVerticalStaggeredGrid(
                modifier = Modifier.heightIn(min= 30.dp,max = 100.dp),
                state = lazyState,
                columns = StaggeredGridCells . Adaptive (80.dp),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ){
                items(listSection){ section ->
                    InputChip(
                        modifier = Modifier.padding(start = 2.dp, end =2.dp),
                        selected = section == selectedSection.value,
                        label = {
                            TextApp(text = section.nameSection,
                                    style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)) },
                        onClick = {
                            if (edit){
                                selectedSection.value = section
                                onClick(selectedSection.value)
                            }
                        }
                    )
                }
            }
        }
    }
}


