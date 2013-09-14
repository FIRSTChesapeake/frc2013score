Matt's FRC 2013 Scoring App
====
Overview
----
This application is meant to be a platform independent replacement
for the scoring half of the FIRST FRC Field Management System.
The second half, robot control, is being designed by a [friend of mine.](/Shoob189).

I've tried to design it in such a way that I will be able to use it from
year to year with as little change as possible.
We'll see how well that works out.

**Note:** You may have to delete your current score DB to upgrade your commit level.

Downloading Release
----
Latest release can be downloaded [here](https://bitbucket.org/crazysane/frc2013score/downloads).
There you will find a Zip of the latest release, as well as a copy of MatchMaker.
Matchmaker is for Windows, but runs well in Wine (I've tried.)
Note the options I've provided in the '.bat' file, this generates the data the way we need it.
I will likely integrate this generator better into the application at some point (maybe?).

if you're just testing, feel free to use the provided matches.txt file (provided with Matchmaker).
The format of this file is, where SM is whether the team is a sourogate that match:
```
<match#> <R1> <R1_SM> <R1> <R1_SM> <R2> <R3_SM> <B1> <B1_SM> <B2> <B2_SM> <B3> <B3_SM>
<match#> <R1> <R1_SM> <R1> <R1_SM> <R2> <R3_SM> <B1> <B1_SM> <B2> <B2_SM> <B3> <B3_SM>
<match#> <R1> <R1_SM> <R1> <R1_SM> <R2> <R3_SM> <B1> <B1_SM> <B2> <B2_SM> <B3> <B3_SM>
```

Download Bleeding Edge
----
Grab the last commit using git:
```
git clone https://bitbucket.org/crazysane/frc2013score.git
```
If you would like to help me with this project, or any other
application I might be working on for non-profits, please send me a message.

TODO
----
- ~~Save modified matches to DB~~
- Design the Edit Options window to formulate event options.
- Create ranking table
- Calculate ranks
- Retrieve Timer Clock from FMS, waiting on [MS.](/Shoob189)
- Come up with option for displays?
- Create process for Finals creation (On-the-fly?)

Matt
