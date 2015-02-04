# jCifs Task for Gradle

copy a file on the cifs server

[![Build Status](https://travis-ci.org/sakamotodesu/jcifs.svg?branch=master)](https://travis-ci.org/sakamotodesu/jcifs)

## How to use

* Gradle 2.2.1 or later
* JRE7 or later

<pre>
task copyCifs(type: github.com.sakamotodesu.jcifs) {
    from "C:\\work"
    into "smb://domain;username:password@server/share/directory/path"
}
</pre>


<pre>
task copyCifs(type: github.com.sakamotodesu.jcifs) {
    from "C:\\work"
    into "smb://domain;username:password@server/share/directory/path"
    include '.*\\.zip'
    exclude '.*\\.txt'
    lmCompatibility '2'
}
</pre>

"include/exclude" is only Regex. They are matched to the file name.


## TODO

- include/exclude change to glob
- other properties

## License

Apache License 2.0

### Modification

All of Groovy code has been modified from [gradle-blank-plugin](https://github.com/int128/gradle-plugin-blank).