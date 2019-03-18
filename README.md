# WeatherDataVisualizer
Visualizer for Weather Sounding Data

Fetches Data from Web - http://meteocentre.com/radiosonde - and stores them locally.
Ability to Plot various associated Paramaters visually.

![Benutzeroberfläche](UserInterface.png)

This programm enables the user to fetch Weather Data from Sounding Probes via the http://meteocentre.com/radiosonde Website.

The User chooses an area and is presented with all available stations within that area (first and second Picker in the first row dubbed "Datapoints"). The following two DatePickers enable the User to choose a timeframe of which he wishes to get the data. Certain possibilities will be excluded. This includes the fact that an end date can't be before a start date and both dates have to be at least the same day as the current day.
The Following Checkbox gives the user to define which dataset of a given day he want´s to import. There are two possibilities because probes are launched at 0:00 local time and 12:00 localtime. If the Checkbox is unchecked the programm fetches the 0:00 data.

The Second line dubbed "Series" enables the user to specify the parameters he want's to plot. The First Picker gives control over the the measured Parameter whereas the second Picker gives control over the pressurelevel at which said measurement was taken. 

The Textfield will be used to create a userdefined name for the choosen parameters and the ColorPicker will be used to change the color of the Series to be created.

The Button "Add Series" will only be available if all parameters which are neccessary to create a series are input by the user as to avoid any non valid input.

The Listview on the right will display every series which is present in the active tab to further manipulate them.

The Tab Area is a LineChart representation of the chosen parameters each Series is presented as a line. Missing data points are extrapolated as to avoid gaps in the graph.
