
           ______               ____                 __
          / / __ \__________   / __ \___  ____ _____/ /__  _____
     __  / / /_/ / ___/ ___/  / /_/ / _ \/ __ `/ __  / _ \/ ___/
    / /_/ / _, _(__  |__  )  / _, _/  __/ /_/ / /_/ /  __/ /
    \____/_/ |_/____/____/  /_/ |_|\___/\__,_/\__,_/\___/_/

***

## Intro
Command line client that will read RSS version 2.0 and some Atom feeds. Refer to [W3C](http://www.w3schools.com/webservices/rss_intro.asp) for more information.

## Build
To build this application:

1. `git clone <repo>`
2. `cd jrss`
3. `mvn install`

NOTE: This project has an active "dev" branch and a "master" branch. If you want the latest features, if applicable, then
      `git checkout dev` and then build. For _stable_ releases build off of the "master" branch.

## Usage
"master" -> `java -jar jrss-1.X-jar-with-dependencies.jar`

"dev" -> `java -jar jrss-1.X-SNAPSHOT-jar-with-dependencies.jar`
