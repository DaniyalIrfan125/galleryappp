
# Gallery Sample App

Fetch the data from storage and shows it in the app exact same format like gallery but with separate images and videos as well.



## Third Party Libraries Used

Four third party libs used in this project

 -> Dexter lib is used for runtime permissions

 -> Facebook shimmer lib is used to add shimmer effect

 -> Photoview lib is used for zoom feature

 -> Glide lib is used to show image
## Code Structure
This app is developed following MVVM architecture pattern.

Applications follows on activity code structure with sharedviewmodel to share data between fragments

Application is having 3 fragments
(MainFragment, GalleryDetailsListFragment, ShowImageFragment)




## Fragment Functionalities
1- MainFragment is created which first checks the runtime permissions on button click. After checking runtime permissions if the permissions are allowed then it calls the method fetchImagesAndVideos which is using flows zip operator to call 2 methods (queryImages and queryVideos) asynchronously and after collecting all the data through collect function of flows then separateFolderFiles method is called which separates all the data according to the respective folder names.
Once all this business logics are done inside view model then the data is being ovberved and shown it in the view using observer pattern.

2- GallerDetailsListFragment is to show the data lists respective to the folder being clicked for example if all images folder is clicked then this fragment will show all the images in list.

3- ShowImageFragment is created to show the imageview on full screen and it has pinch to zoom feature as well same like gallery app.



## 🚀 About Me
Daniyal Irfan (Android App Developer)

