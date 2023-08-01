package com.example.shopping_list.entity



enum class SortingBy{
    BY_NAME,
    BY_SECTION
}
enum class ColorApp(index: Long){
    ScaffoldColor (0xFFECECEC) ,
    SelectedColor (0xFF595F7E),
    Secondary (0xFF03A9F4),
    PrimaryVariant (0xBCEE1616),
    OnPrimaryOnSecondaryOnError (0xFF009688),
    BackgroundBottomSheet (0xFFF0F0F0),
    TextIcon (0xFF464646),
    TextDate (0xFF9C9C9C),
    BackgroundBottomBar (0xFFCECECE),
    BorderBottomBar (0xFF464646),
    BackgroundElementList (0x9FFFFFFF),
    SectionColor (0x22909EE7),
    SectionColor1 (0xFFFFA200),
    BackgroundFab (0xFFF0F0F0),
    ContentFab (0xFF464646),
    ButtonColorsMy (0xffcccccc)
}

enum class TypeText(){
    NAME_SCREEN,
    NAME_SECTION,
    TEXT_IN_LIST,
    TEXT_IN_LIST_SMALL,
    EDIT_TEXT,
    EDIT_TEXT_TITLE,
    TEXT_IN_LIST_SETTING,
    NAME_SLIDER
}