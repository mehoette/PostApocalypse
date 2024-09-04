
package com.zybooks.HonorsProject

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val board = findViewById<RelativeLayout>(R.id.board)
        val border = findViewById<RelativeLayout>(R.id.relativeLayout)
        val lilu = findViewById<LinearLayout>(R.id.lilu)
        val upButton = findViewById<ImageView>(R.id.up)
        val downButton = findViewById<ImageView>(R.id.down)
        val leftButton = findViewById<ImageView>(R.id.left)
        val rightButton = findViewById<ImageView>(R.id.right)
        val pauseButton = findViewById<ImageView>(R.id.pause)
        val newgame = findViewById<ImageView>(R.id.new_game)
        val playagain = findViewById<Button>(R.id.playagain)
        val monster1 = ImageView(this)
        val monster2 = ImageView(this)
        val monster3 = ImageView(this)
        val avatar = ImageView(this)
        val npc = ImageView(this)
        val handler = Handler()
        var delayMillis = 3L // Update postal worker position every 100 milliseconds
        var currentDirection = "up" // Start moving up by default
        var scorex = 0
        var hiding: Boolean = false //are you hiding right now?
        var lastDirection = "up" //what was the last direction you were going. up by default
        var monster1Up = true //should the monster go up
        var monster2Up = true //should the monster go up
        var monster3Up = true //should the monster go up
        var monsterSquare = 1 //one monster will go in a square so the regular boolean for lines won't work for it
        var screen = 1 //this will store what current screen we're on. It starts at 1 because that's the screen we start at
        var deliveries = 0 //this counts the number of deliveries you have, once you get to 3 you win.
        //you can only make each delivery once
        var deliveryRichard = false
        var deliveryMaggie = false
        var deliveryNewt = false


        board.visibility = View.INVISIBLE
        playagain.visibility = View.INVISIBLE

        newgame.setOnClickListener {
            border.setBackgroundColor(getResources().getColor(R.color.brown))

            board.visibility = View.VISIBLE
            newgame.visibility = View.INVISIBLE


            //lets set up the avatar
            avatar.setImageResource(R.drawable.postal_worker)
            avatar.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            board.addView(avatar)

            //and place them on the board
            avatar.x = 0f
            avatar.y = 0f
            var avatarX = avatar.x
            var avatarY = avatar.y




            //lets set up all 3 monsters
            monster1.setImageResource(R.drawable.monster)
            monster1.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            monster2.setImageResource(R.drawable.monster)
            monster2.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            monster3.setImageResource(R.drawable.monster)
            monster3.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            //we'll set the npc to be richard initially
            npc.setImageResource(R.drawable.richard)
            npc.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


            //and place them all on the board
            board.addView(monster1)
            board.addView(monster2)
            board.addView(monster3)
            board.addView(npc)


            fun die(){
                border.setBackgroundColor(getResources().getColor(R.color.red))
                playagain.visibility = View.VISIBLE
                currentDirection = "pause"
                lilu.visibility = View.INVISIBLE
                deliveries = 0

            }

            fun deliver(){
                border.setBackgroundColor(getResources().getColor(R.color.green))
                if(screen == 3 && !deliveryRichard){
                    deliveryRichard = true
                    deliveries++
                }
                else if(screen == 4 && !deliveryMaggie){
                    deliveryMaggie = true
                    deliveries++
                }
                else if(screen == 5 && !deliveryNewt){
                    deliveryNewt = true
                    deliveries++
                }


                if(deliveries == 3){
                    win()
                }
            }

            fun checkEnemyCollision() {

                val distanceThreshold = 125 //how far away do you have to be from the monster

                //check and see if you're close to monster1
                var distance = sqrt((avatar.x - monster1.x).pow(2) + (avatar.y - monster1.y).pow(2))

                if (distance < distanceThreshold && !hiding) { //if the distance between the avatar and the monster is less than the damage threshold and you aren't hiding. if you're hiding, the monster can't hurt you

                    die()

                }

                //check and see if you're close to monster2
                distance = sqrt((avatar.x - monster2.x).pow(2) + (avatar.y - monster2.y).pow(2))

                if (distance < distanceThreshold && !hiding) { //if the distance between the avatar and the monster is less than the damage threshold and you aren't hiding. if you're hiding, the monster can't hurt you

                    die()

                }

                //check and see if you're close to monster3
                distance = sqrt((avatar.x - monster3.x).pow(2) + (avatar.y - monster3.y).pow(2))

                if (distance < distanceThreshold && !hiding) { //if the distance between the avatar and the monster is less than the damage threshold and you aren't hiding. if you're hiding, the monster can't hurt you

                    die()

                }

                //check and see if you're close to npc
                distance = sqrt((avatar.x - npc.x).pow(2) + (avatar.y - npc.y).pow(2))

                if (distance < distanceThreshold && !hiding) { //if the distance between the avatar and the monster is less than the damage threshold and you aren't hiding. if you're hiding, the monster can't hurt you

                    Log.d("COMPLETE", "delivered letter")
                    deliver()
                }
            }



            fun moveMonsters1(){
                //monster 1 just goes in a vertical line
                if(monster1Up){
                    monster1.y += 2
                }
                else{
                    monster1.y -= 2
                }

                if ((monster1.y > 700f && monster1Up)|| (monster1.y < 100f && !monster1Up)){
                    monster1Up = !monster1Up
                }
            }

            fun moveMonsters2(){
                //monster 1 goes in a diagonal line
                if(monster1Up){
                    monster1.y += 2
                    monster1.x += 2
                }
                else{
                    monster1.x -=2
                    monster1.y -= 2
                    }

                if ((monster1.x > 500f && monster1Up)|| (monster1.x < -100f && !monster1Up)){
                    monster1Up = !monster1Up
                }

                //monster 2 goes in a horizontal line
                if(monster2Up){
                    monster2.x += 2
                }
                else{
                    monster2.x -= 2
                }

                if ((monster2.x > 750f && monster2Up)|| (monster2.x < 50f && !monster2Up)){
                    monster2Up = !monster2Up
                }
            }

            fun moveMonsters3(){
                //monster 1 goes in a square
                //starts at 1 goes up to 4 repeat
                if(monsterSquare == 1){
                    monster1.x -= 5
                }
                else if(monsterSquare == 2){
                    monster1.y += 5
                }
                else if(monsterSquare == 3){
                    monster1.x += 5
                }
                else if(monsterSquare == 4){
                    monster1.y -= 5
                }

                //once you get to each corner, turn to the next side
                if(monster1.x < 50 && monsterSquare == 1){
                    monsterSquare = 2
                }
                else if(monster1.y > 750 && monsterSquare == 2){
                    monsterSquare = 3
                }
                else if(monster1.x > 750 && monsterSquare == 3){
                    monsterSquare = 4
                }
                else if(monster1.y < 50 && monsterSquare == 4){
                    monsterSquare = 1 //loop back up to 1
                }
            }

            fun moveMonsters4(){
                //monster 1 goes in a horizontal line
                if(monster1Up){
                    monster1.x += 2
                }
                else{
                    monster1.x -= 2
                }

                if ((monster1.x > 750f && monster1Up)|| (monster1.x < 50f && !monster1Up)){
                    monster1Up = !monster1Up
                }

                //monster 2 also goes in a horizontal line
                if(monster2Up){
                    monster2.x += 2
                }
                else{
                    monster2.x -= 2
                }

                if ((monster2.x > 750f && monster2Up)|| (monster2.x < 50f && !monster2Up)){
                    monster2Up = !monster2Up
                }
            }


            fun showScreen1(){
                border.setBackgroundColor(getResources().getColor(R.color.brown))
                screen = 1
                monster1Up = true
                monster2Up = true
                Log.d("Screen = ", "1")

                //he starts off a little to the right center of page
                monster1.x = 300f
                monster1.y = 0f

                //we don't need monsters 2 and 3 for this screen so get them out of the way
                monster2.x = 1500f
                monster2.y = 1500f
                monster3.x = 1500f
                monster3.y = 1500f

                //there's also no npcs on this screen
                npc.x = 1500f
                npc.y = 1500f
            }

            showScreen1() //we want to call show screen 1 right away because that's the screen we start on

            fun showScreen2(){

                border.setBackgroundColor(getResources().getColor(R.color.brown))
                monster1Up = true
                monster2Up = true
                screen = 2
                Log.d("Screen = ", "2")

                //monster 1 starts in the top left corner
                monster1.x = 800f
                monster1.y = 800f

                //monster 2 starts in the middle of the screen on the right side
                monster2.y = 400f
                monster2.x = 800f

                //we don't need monster 3 or npc for this screen so get them out of the way
                monster3.x = 1500f
                monster3.y = 1500f
                npc.x = 1500f
                npc.y = 1500f

            }

            fun showScreen3(){

                border.setBackgroundColor(getResources().getColor(R.color.brown))
                monster1Up = true
                screen = 3
                Log.d("Screen = ", "3")

                //we want the monster to start in the center middle
                monster1.x = 300f
                monster1.y = 50f

                //the npc in area 3 is richard
                npc.setImageResource(R.drawable.richard)
                npc.x = 450f
                npc.y = 450f


                //we don't need monsters 2 and 3 for this screen so get them out of the way
                monster2.x = 1500f
                monster2.y = 1500f
                monster3.x = 1500f
                monster3.y = 1500f
            }

            fun showScreen4(){
                border.setBackgroundColor(getResources().getColor(R.color.brown))
                screen = 4
                monster1Up = true
                monster2Up = false
                Log.d("Screen = ", "4")

                //we want the monster to start in first third on the left
                monster1.x = 50f
                monster1.y = 300f

                //we want the monster to start in second third on the right
                monster2.x = 750f
                monster2.y = 500f

                //the npc in area 4 is maggie
                npc.setImageResource(R.drawable.maggie)
                //and she's in the center bottom
                npc.x = 450f
                npc.y = 750f


                //we don't need monster 3 for this screen so get them out of the way
                monster3.x = 1500f
                monster3.y = 1500f
            }
            fun showScreen5(){
                border.setBackgroundColor(getResources().getColor(R.color.brown))
                screen = 5
                Log.d("Screen = ", "4")

                //we want the monster to start in first third on the left
                monster1.x = 250f
                monster1.y = 200f

                //we want the monster to start in second third on the right
                monster2.x = 740f
                monster2.y = 400f

                monster3.x = 500f
                monster3.y = 700f

                //the npc in area 4 is newt
                npc.setImageResource(R.drawable.newt)
                //and they're in the center bottom
                npc.x = 750f
                npc.y = 50f

            }

            fun moveMonsters(){
                //depending on what stage you're on do different move monsters.
                if(screen == 1){
                    moveMonsters1()
                }
                else if (screen == 2){
                    moveMonsters2()
                }
                else if (screen == 3){
                    moveMonsters3()
                }
                else if (screen == 4){
                    moveMonsters4()
                }
            }
            val runnable = object : Runnable { //this is the game loop that repeats while the game is going on.
                override fun run() {


                    if(!hiding) {//you can only move when you aren't hiding
                        when (currentDirection) {
                            "up" -> {

                                lastDirection = "up"
                                if (avatarY < -400) { // Check if the ImageView goes off the top of the board
                                    Log.d("HIT SIDE", "up")
                                    if(screen == 2){
                                        avatarY = 500f
                                        showScreen3()
                                    }
                                    else if(screen == 4){
                                        avatarY = 500f
                                        showScreen2()
                                    }
                                }
                                else{
                                    avatarY -= 5
                                }

                                avatar.translationY = avatarY
                            }

                            "down" -> {
                                lastDirection = "down"
                                if(avatarY < 500) {
                                    avatarY += 5
                                }
                                else { // Check if the ImageView goes off the bottom of the board
                                    Log.d("HIT SIDE", "down")
                                    if(screen == 3){
                                        avatarY = -400f
                                        showScreen2()
                                    }
                                    else if(screen == 2){
                                        avatarY = -400f
                                        showScreen4()
                                    }
                                }
                                avatar.translationY = avatarY
                            }

                            "left" -> {
                                lastDirection = "left"
                                if (avatarX < -400) { // Check if the ImageView goes off the top of the board
                                    Log.d("HIT SIDE", "left")
                                    if(screen == 2){
                                        avatarX = 500f
                                        showScreen1()
                                    }
                                    else if (screen == 5){
                                        avatarX = 500f
                                        showScreen2()
                                    }
                                }
                                else{
                                    avatarX -= 5
                                }
                                avatar.translationX = avatarX
                            }

                            "right" -> {
                                lastDirection = "right"
                                if (avatarX > 500) { // Check if the ImageView goes off the bottom of the board
                                    Log.d("HIT SIDE", "right")
                                    if(screen == 1){
                                        avatarX = -400f
                                        showScreen2()
                                    }
                                    else if(screen == 2){
                                        avatarX = -400f
                                        showScreen5()
                                    }
                                }
                                else{
                                    avatarX += 5
                                }
                                avatar.translationX = avatarX
                            }

                            "pause" -> {
                                avatarX += 0
                                avatar.translationX = avatarX
                            }
                        }
                    }
                    checkEnemyCollision()
                    moveMonsters()
                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.postDelayed(runnable, delayMillis)

            // Set button onClickListeners to update the currentDirection variable when pressed
            upButton.setOnClickListener {
                currentDirection = "up"
            }
            downButton.setOnClickListener {
                currentDirection = "down"
            }
            leftButton.setOnClickListener {
                currentDirection = "left"
            }
            rightButton.setOnClickListener {
                currentDirection = "right"
            }
            pauseButton.setOnClickListener {
                if(hiding != true){
                    currentDirection = "pause"
                    avatar.setImageResource(R.drawable.box)
                    Log.d("x: " + avatar.x, "y: " + avatar.y)
                }
                else{
                    currentDirection = lastDirection
                    avatar.setImageResource(R.drawable.postal_worker)
                }
                hiding = !hiding

            }
            playagain.setOnClickListener {

                recreate()
            }

        }


    }
    fun win(){
        setContentView(R.layout.activity_win)

        val btn = findViewById<ImageView>(R.id.play_again)

        btn.setOnClickListener {
            recreate()
        }
    }

}

