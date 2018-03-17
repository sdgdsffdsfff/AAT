#!/bin/bash

DRAW="../app/src/main/res/drawable"

DRAWABLES="$DRAW/"
LDPI="$DRAW-ldpi/"
MDPI="$DRAW-mdpi/"
HDPI="$DRAW-hdpi/"
XHDPI="$DRAW-xhdpi/"
XXHDPI="$DRAW-xxhdpi/"
XXXHDPI="$DRAW-xxxhdpi/"


function COPY {
    TARGET="${1//-/_}".png

	convert $1.png -scale "36x36"   $LDPI/$TARGET
	convert $1.png -scale "48x48"   $MDPI/$TARGET
	convert $1.png -scale "72x72"   $HDPI/$TARGET
	convert $1.png -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -scale "192x192" $XXXHDPI/$TARGET

}


function INVERT {
	TARGET="${1//-/_}_inverse.png"

	convert $1.png -negate -scale "36x36"   $LDPI/$TARGET
	convert $1.png -negate -scale "48x48"   $MDPI/$TARGET
	convert $1.png -negate -scale "72x72"   $HDPI/$TARGET
	convert $1.png -negate -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -negate -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -negate -scale "192x192" $XXXHDPI/$TARGET

}


function HIGHLIGHT {
	TARGET="${1//-/_}_highlight.png"

	convert $1.png -negate -scale "36x36"   $LDPI/$TARGET
	convert $1.png -negate -scale "48x48"   $MDPI/$TARGET
	convert $1.png -negate -scale "72x72"   $HDPI/$TARGET
	convert $1.png -negate -scale "96x96"   $XHDPI/$TARGET
	convert $1.png -negate -scale "144x144" $XXHDPI/$TARGET
	convert $1.png -negate -scale "192x192" $XXXHDPI/$TARGET

}

function CCONVERT {
	xcf2png $1.xcf > $1.png
	COPY $1
}


function CONVERT {
	xcf2png $1.xcf > $DRAWABLES/$1.png
}



function CLEAR_DIRECTORY {
    if [ ! -d "$1" ]; then
        echo "$1 does not exist."
        exit 1
    fi

    echo "Remove all Files in $1"
    rm $1*
}

CLEAR_DIRECTORY $DRAWABLES
CLEAR_DIRECTORY $LDPI
CLEAR_DIRECTORY $MDPI
CLEAR_DIRECTORY $HDPI
CLEAR_DIRECTORY $XHDPI



####### Global
cp button.xml $DRAWABLES
CONVERT "button_default.9"
CONVERT "button_pressed.9"
CONVERT "button_selected.9"

####### Application
COPY "icon" 
CONVERT "status"
CONVERT "icon_status"


####### Preview
COPY "open-menu-light"

####### Top-Bar
INVERT "edit-undo" 
INVERT "go-next" 
INVERT "go-previous" 
INVERT "go-down" 
INVERT "go-up" 
INVERT "open-menu"
INVERT "edit-select-all"
INVERT "folder"


####### Navigation-Bar
COPY "zoom-fit-best" 
COPY "zoom-in" 
COPY "zoom-out" 
COPY "zoom-original" 
INVERT "zoom-original"

####### File-Bar
COPY "edit-select-all"
COPY "view-paged"


####### Edit-Bar
COPY "go-up" 
COPY "go-down" 
COPY "document-save-as" 
COPY "edit-clear-all" 
COPY "document-save" 
COPY "edit-undo" 
COPY "user-trash" 
COPY "edit-redo" 
COPY "list-add" 
COPY "list-remove" 
COPY "gtk-convert" 


####### Information-Bar
COPY "view-grid" 
COPY "dialog-question" 
COPY "go-bottom"  ## Overpass
COPY "edit-find"  ## Nominatim
COPY "view-refresh"  
COPY "find-location"


####### Tile-Remover
INVERT "view-refresh"
INVERT "user-trash"


####### Tracker / Menu
INVERT "media-playback-pause" 
INVERT "media-playback-start" 
INVERT "media-playback-stop" 



####### FileView / OsmApi Activities
INVERT "view-paged"
INVERT "content-loading"
INVERT "go-bottom"  ## Overpass
INVERT "edit-find"  ## Nominatim
INVERT "edit-clear-all" 
INVERT "document-save-as" 

