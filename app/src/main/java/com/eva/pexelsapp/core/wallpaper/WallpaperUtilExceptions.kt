package com.eva.pexelsapp.core.wallpaper

class WallpaperModeInvalidException :
	Exception("This format is not supported with this api use the other method")

class WallpaperChangeNotAllowedException :
	Exception("Cannot change the wallpaper")