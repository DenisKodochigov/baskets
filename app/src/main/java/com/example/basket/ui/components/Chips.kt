package com.example.basket.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.TypeText
import com.example.basket.entity.UnitApp
import com.example.basket.ui.theme.styleApp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun ChipsUnit(
    listUnit: List<UnitApp>,
    edit: Boolean,
    onClick: (UnitApp) -> Unit,
    unitArticle: UnitApp? )
{
    var selectedUnit by remember { mutableStateOf<UnitApp>(UnitDB())}
    if (unitArticle != null) selectedUnit = unitArticle

    Column{
        Text(text = stringResource(id = R.string.units),
            style = styleApp(TypeText.NAME_SLIDER),
            modifier = Modifier.padding(start = 12.dp))
        Box(
            modifier = Modifier
                .padding(top = 0.dp)
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
                        selected = unit == selectedUnit,
                        label = {
                            Text(text = unit.nameUnit, maxLines = 1, textAlign = TextAlign.Center,
                                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)) },
                        onClick = {
                            if (unitArticle == null || (unitArticle != null && edit) ){
                                selectedUnit = unit
                                onClick(selectedUnit)
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
    edit: Boolean,
    listSection: List<Section>,
    sectionArticle: Section?,
    onClick: (Section) -> Unit) {

    val lazyState = rememberLazyStaggeredGridState()
    var selectedSection by remember { mutableStateOf<Section>(SectionDB())}
    if (sectionArticle != null) selectedSection = sectionArticle
    Column {
        Text(
            text = stringResource(id = R.string.sections),
            style = styleApp(TypeText.NAME_SLIDER),
            modifier = Modifier.padding(start = 12.dp)
        )
        Box(modifier = Modifier)
        {
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            LazyVerticalStaggeredGrid(
                modifier = Modifier.heightIn(min= 20.dp,max = 80.dp),
                state = lazyState,
                columns = StaggeredGridCells . Adaptive (80.dp),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ){
                items(listSection){ section ->
                    InputChip(
                        modifier = Modifier.padding(start = 2.dp, end =2.dp),
                        selected = section == selectedSection,
                        label = { Text(text = section.nameSection,
                            maxLines = 1, textAlign = TextAlign.Center,
                            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)) },
                        onClick = {
                            if (sectionArticle == null || (sectionArticle != null && edit)){
                                selectedSection = section
                                onClick(selectedSection)
                            }
                        }
                    )
                }
            }
        }
    }
}


