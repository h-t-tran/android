1. When you run your app and rotate the device/emulator are the method displayed in the TextView consistent with methods called in the log? If not what would you have to do to make them consistent?
 
The display on the textview has less methods than in Logcat.  For example when we go from portrait to landscape, the logcat displays

onPause()
onSaveInstanceState()
onCreate()
onStart()
onRestoreInstanceState()
onResume()
The textview displays
onCreate()
onStart()
onRestoreInstanceState()
onResume()

The reason is the onPause and onSaveInstanceState() belongs to previous instance of the activity.  When the rotation occurs, the onPause and onSaveInstanceState occurs, but then the new landscape activity starts which creates a new TextView which does not contain these 2 methods.  The remedy to this problem is to persist the content of the TextView within onRestoreInstanceState() method and restore it within onRestoreInstanceState().




