package utilz;

import herorescue.Game;

public class Constants {

    public static final float GRAVITY = 0.04f * Game.scale;
    public static final int ANI_SPEED = 25;

    public static class ObjectConstants{
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SWORD = 4;
        public static final int SHIELD = 5;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int SWORD_VALUE = 30;
        public static final int SHIELD_VALUE = 120;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.scale * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.scale * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.scale * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.scale * POTION_HEIGHT_DEFAULT);

        public static final int ITEM_WIDTH_DEFAULT = 64;
        public static final int ITEM_HEIGHT_DEFAULT = 64;
        public static final int ITEM_WIDTH = (int) (ITEM_WIDTH_DEFAULT);
        public static final int ITEM_HEIGHT = (int) (ITEM_HEIGHT_DEFAULT);
        
        public static int GetSpriteAmount(int object_type){
            switch(object_type){
            case RED_POTION, BLUE_POTION:
                return 7;
            case BARREL, BOX:
                return 7;
            case SHIELD, SWORD:
                return 7;
            }

            return 1;
        }
    
    }

    public static class EnemyConstants{
        public static final int LIZARD = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int LIZARD_WIDTH_DEFAULT = 128;
        public static final int LIZARD_HEIGHT_DEFAULT = 64;

        public static final int LIZARD_WIDTH = (int) (LIZARD_WIDTH_DEFAULT);
        public static final int LIZARD_HEIGHT = (int) (LIZARD_HEIGHT_DEFAULT);

        public static final int LIZARD_DRAWOFFSET_X = (int) (26 * Game.scale);
        public static final int LIZARD_DRAWOFFSET_Y = (int) (9 * Game.scale);

        public static int GetSpriteAmount(int enemy_type, int enemy_state){

            switch(enemy_type){
            case LIZARD:
                switch(enemy_state){
                case IDLE:
                    return 3;
                case RUNNING:
                    return 6;
                case ATTACK:
                    return 4;
                case HIT:
                    return 2;
                case DEAD:
                    return 6;
                }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case LIZARD:
                    return 20; //เลือดของ enemy
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case LIZARD:
                    return 15; //damage ของ enemy
                default:
                    return 0;  
            }
        }
    }

    public static class UI {
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.scale);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.scale);
        }

        public static class URMButtons{
        public static final int URM_DEFAULT_SIZE = 56;
        public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.scale);
       
    }
    }


    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0; //ท่าทางเริ่มต้นของอัศวิน
        public static final int ATTACK = 3; //ท่าเวลาโจมตีโดยจะใช้แถวที่ 3 จาก sprite ทั้งหมด
        public static final int JUMP = 2;
        public static final int RUNNING = 1;

        public static int GetSpriteAmount(int player_action) {

            switch (player_action) {
                case RUNNING:
                    return 6;
                case JUMP:
                case IDLE:
                case ATTACK:
                    return 2;
                default:
                    return 1;
                }
        }
    }
}