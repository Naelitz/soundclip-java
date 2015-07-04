# SoundClip - Simple, Sound Cue Management

**PRE-ALPHA**: This software doesn't even do anything right now. I'm just experimenting with re-implementing 
[the current python implementation](https://github.com/techwiz24/soundclip) in javaFX

## Building
Use `maven` to generate the jar:

```
mvn package
```

This will generate `target/soundclip-<VERSION>.jar` and `target/soundclip-<VERSION>-jar-with-dependencies.jar`. Use the
former if you already have the dependencies on your classpath, use the later if you want an all-in-one solution:

```
java -jar target/soundclip-<VERSION>-jar-with-dependencies.jar
```

See `pom.xml` for dependencies.

## Running
If you want to skip building the jar, you can run the program directly:

```
mvn exec:java -Dexec.mainClass="soundClip.SoundClip"
```

## License
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Projects are your own creation. You may do with them what you wish.
If you distribute SoundClip with your project, you must do so under
the terms of the LICENSE.

Plugins may have their own separate license. Contact the plugin author
or consult the documentation for details.

## Creating Custom Cues
Add the main `jar` to your class path and extend the `soundclip.api.Cue` class. Your custom cue must also be annotated
with the `soundclip.annotation.@CueMeta` annotation. Compiled cues can be placed in the following locations:

* `$SOUNDCLIP_HOME/extensions` - System-wide Cue Plugins
* `$HOME/.soundclip/extensions` - User-local Cue Plugins
* `/path/to/some/project/.soundclip/extensions` - Project-local Cue Plugins