# Tic Tac Toe

Tic tac toe built using Clojurescript and Reagent. Coded using vim and figwheel.

## Build Log

**April 22, 2016**

Project bootstrapped with:

    lein new figwheel tic-tac-toe -- --reagent

Application state is isolated in a single atom called `app-state`. This atom is a map with keyword symbols which currently contains the `:game-board` (vec of vecs), `:board-size`/`:win-length` (ints) and `:game-status` (keyword).

Board renders to the browser.

**April 23, 2016**

Added a `def`'d map for all global constants like min/max board size and min/max board length.

Have to decided on a constraint that only component functions and app-state mutation functions can be impure. Components can access global constants and global app state by way of explicit `let` forms. All functions that mutate `app-state` must have names that end in `!` and must only be called by components. All other functions will remain pure and therefore testable.

Introduced local state within the `new-game-component` by way of `let` defined atoms for user selected board size and win length. These atoms are bound to dropdown components and are mutated by these components on change. A button in the `new-game-component` calls `reset-app-state!` while deref'ing these local atoms as arguments.

User can now reset the game, and in the process, change the board size and win length. On reset the newly resized board is rendered to the browser.

**April 24, 2016**

Struggling with how to implement player moves when a blank space is clicked since app state mutation is required.

Based on my constraint from yesterday about function purity the `blank-space-component` should mutate the game board. It should only allow a move to be played if the game is still active (no one has won yet). After a player move has been played, a cpu move needs to be calculated and played too, resulting in another board mutation. To simplify things I might bundle those together and only mutate the game-board once. That's fine for a simple cpu player, but if a cpu move took time to compute the player's move wouldn't render until the cpu move was played.

After either player or cpu moves the board must be checked for a win/draw, which means another possible mutation to the game status in the app state.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## Unlicense

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
