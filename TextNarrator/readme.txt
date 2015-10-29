MileStone 3:


* List 0f Tasks Completed
1) Updated ListView UI.
 -> Added more details like the size of the file, and the path of the file in listview.
 - >Displays the name of the file.(Implemented in milestone1)
- >Displays the first page of the pdf as a bitmap in listview.(Implemented in milestone1)



2) Added pause functionality.



3) Added stop button to action bar of PdfTextActivity.



4)Added menu to the PdfTextActivity (where the pdf text is displayed). 
    The menu gives the following options
  ->-Go to Page: Opens a dialog that allows user to enter the page. And goes to the page entered if its a valid page number


  ->(Bookmark: Opens a submenu with the following options: -(Priya Bhatt)
        a)View Bookmarks:
                Gives the list of page numbers  in a dropdown spinner already bookmarked by the user. Goes to the page selected by the user.
        b)BookMark the page :
                Bookmarks the current page open in the view.
        
  ->Settings:
        Added two preference settings.
        a)Set speech rate: -(Shilpi Singh)
                Opens a dialog with a seekbar ranging from 0.2 to 2.5 which provides the rate of speech for the narration.
        b)Set Focus Color:-(Priya Bhatt)
                Opens a dialog with a spinner drop down which provides different color choices for the highlight color of the text.


5)Other UI Enhancements:
   a) Created a help screen and added application icon : Displays the basic flow of the application to the user
 -(Shilpi SIngh).
  b) Created a startup Screen : Developed startup screen using gradient.



lessons learned on Each of the tasks:


1) : To change the custom listview, display different file parameters size and path.
2) : Play/Pause the narration using a toggle button in the action bar.
3) : To implement stop functionality.
4)-> Provide options such as Bookmarks and “Go to”.
 ->Implement various Settings Using Preferences,Preference Activity and designing custom Preference for speech rate and Color.
 ->Learn how to use Preference Activity, Custom Preferences.

        






Milestone 2:




* List 0f Tasks Completed


1) Developed a Context Menu for the items in the pdf List. When the user does 
"Long Press" action  on any item , it displays the menu with the following options.
            
->View as text
                
->View as Pdf 




2)  Added features to the existing activity(launched when the user clicks "View as text" in the context menu) which is launched 4
when the user clicks on option "View as text".
    
->Instead of displaying the text of the entire pdf (MileStone 1), we displayed the text of only the first page.






3) Implemented "Gesture Listner" that lets user go to next page(on left swipe) or previous page(on right swipe) of the pdf. 




4) Added "play and pause" a single toggle button on the action bar.







5) Added play functionality, the text on the page is read in the form of speech by text to speech engine "line by line 
using OnUtteranceProgressListner.


 
6) Highlight the text "line by line" as it is being read.







7) Created a new activity, which is launched when the user clicks "View as Pdf" in the context menu.


-(Priya Bhatt)


8) The new activity displays the pdf"s first page as a bitmap image.  Implemented " Gesture Listner " that lets user 
go to next page(on left swipe) or previous page(on right swipe) of the pdf.







 




* lessons learned on each of the above task




1) To learn and develop context menu.


2)  Change the implementation, of the previous activity and display the text of only the first page, display the page No at the end of the page.


3)To learn and implement "Gesture Listner" that lets user scroll/swipe to next and previous page on left and right motion/fling.


4) Implemented action bar with "play and pause" toggle button. 


5) Read the text from the page "line by line", using OnUtteranceProgressListner. Learn how to implement OnUtteranceProgressLIstner.


6) Highlight the text line by line using spannable. Learnt how to use spannable to highlight subtext of a textView.


7) To implement a new activity launched when the user clicks "View as Pdf" on the context menu, and display the pdf  page(as it as)  and
 gesture listener to allow user to scroll/swipe to next or previous page using left or right swipe motion.






Milestone 1:




* List 0f Tasks Completed




1)Retrieve all pdf files from the device’s external storage using Media Provider.
2)Retrieve  the first page of each pdf file as a bitmap using external library qoppapdf.jar(qpdf)
3)Develop a custom list where every item in list has two attributes:
        a) The first page as a thumbnail.
        b) The name of the pdf.
        c)If the pdf is of size 0 bytes then it displays a default image.
4)On click of the items in the list it results in the launch of a new activity.
5)The new activity extracts the text from the pdf and displays it. We used an external library itext for this purpose.








lessons learned on each of the above task
1)  to retrieve pdf files using media provider.
 (Before it was covered in class, it took us time to search about it and understand how to use it , but after it was taught in class it helped us to gain better conceptual understanding. )




2)to retrieve first page of pdf as a bitmap :
To understand and test the different API’s of the external library and use the appropriate API to retrieve the first page as a bitmap.




3) to display the list with name and the bitmap and check error conditions like if the pdf is of size 0 bytes, then display a default image.




4) 
To understand  and test the different API’s of the external library itext and use the appropriate one to extract text from the pdf and display it.




6)4 Days: (Research)(Shilpi Singh and Priya Bhatt)
The challenging part was try to have a pdf viewer which took most of the time.


 spent time on the external java library pdf box and tried to make it work which we realized later only works for java applications not on android. We tried other external libraries including itext, qoppapdf.jar (qpdf), PdfViewer.jar and itext and qoppapdf libraries worked for us.



















REFERENCES:
http://www.qoppa.com/files/android/pdfsdk/guide/javadoc/
http://api.itextpdf.com/itext/
Class Notes: To understand how to use custom adapter.








