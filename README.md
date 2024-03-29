          ##            CCCCCCCCCCC
          ##         CCCCCCCCCCCCCCCCC 
          ##       CCCCC          CCCC
          ##     CCCC                 CCCCCCCCC 
          ##    CCCC    @@      @@    CCCCCC 
          ##    CCC    @@      @@     CCC 
          ##    CCC   @@@@@   @@  @@  
          ##    CCC  @@   @@  @@@@@@@ CCC
          ##    CCCC  @@@@@      @@   CCCCCC
     ##   ##     CCCC                 CCCCCCCCC 
     ##   ##       CCCCC          CCCC              
     ##  ##         CCCCCCCCCCCCCCCCC
     ####             CCCCCCCCCCC


## This is JC64: a C64 Java Software Emulator

note: this program is under construction, actually no executable are released
to the public.

The JC64 is an emulator that try to 100% emulate a C64 (with the actual
knowledge about the various C64 chips) using the Java language. 
You must have a powerful system to run this program. Sorry, the real emulation
is the priority goal, unlike the speed emulation is the secondary goal.

The Java language is used due to his multiplatform and OOP characteristics.

The emulator is Threads based (multi-cpu system can take advantage of this).

The actual Vic code is very experimental: uses of dot clock for a 1 to 1
implementation of Vic II chip.

Copyright notice
----------------

This project is
  Copyright © 1999-2001 Ice Team Free Software Group

These are the Ice Team members

  Copyright © 1999-2002 Stefano Tognon
  Copyright © 2000      Michele Caira

The cpu instruction emulation is based on the

 VICE, the Versatile Commodore Emulator

   Copyright © 1996-1999 Ettore Perazzoli
   Copyright © 1996-1999 André Fachat
   Copyright © 1993-1994, 1997-1999 Teemu Rantanen
   Copyright © 1997-1999 Daniel Sladic
   Copyright © 1998-1999 Andreas Boose
   Copyright © 1998-1999 Tibor Biczo
   Copyright © 1993-1996 Jouko Valta
   Copyright © 1993-1994 Jarkko Sonninen

Other information are taken on:

 Vic Article
   Copyright © 1996 Christian Bauer

 64doc
   Copyright © 1993-1994 John West, Marko M"akel"a

 Pal Timing
   Copyright © 1994 Marko M"akel"a

 Commodore manuals
   Copyright © Commodore Business Machine

---------------------------------------------------

## JC64dis  (beta release 1 of 2003):

Inside the program there is the JC64dis (beta release 1 of 2003) 
that disasseble PRG,SID and MUS file and produce a output file.

The class to run is:
  sw_emulator.software.FileDasm 

---------------------------------------------------  
  
## JC64dis (Next generation C64 disassembler)

Inside the program there is the new JC64dis with a graphical
interface and an innovative 3 iterative panels to let you 
make a source code that can be understand by humans. 

The class to run is:
  sw_emulator.swing.main.JC64dis
  
--------------------------------------------------- 

This project uses:
## YourKit Java Profiler 
![YourKit](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with innovative and intelligent tools for monitoring and profiling Java and .NET applications.

YourKit is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/), [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/), [YourKit YouMonitor](https://www.yourkit.com/youmonitor/)
  
