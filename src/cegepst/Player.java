package cegepst;

import cegepst.engine.Buffer;
import cegepst.engine.CollidableRepository;
import cegepst.engine.controls.Direction;
import cegepst.engine.entity.ControllableEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class Player extends ControllableEntity {

    private static final String SPRITE_PATH = "images/PlayerSprites.png";
    private GamePad gamePad;
    private SpriteReader spriteReader;
    private BufferedImage spriteSheet;
    private Animator animator;
    private Image[] gunningRight;
    private Image[] runningRight;
    private Image[] jumpingRight;
    private Image[] gunningLeft;
    private Image[] runningLeft;
    private Image[] jumpingLeft;
    private Image[] currentActiveSprites;
    private BufferedImage crouchRight;
    private BufferedImage crouchLeft;
    private BufferedImage deathSprite;
    private BufferedImage currentActiveSprite;
    private int fireCooldow;

    public Player(GamePad gamePad) {
        super(gamePad);
        this.gamePad = gamePad;
        super.setDimension(30, 30);
        super.setSpeed(2);
        initClassContent();
        CollidableRepository.getInstance().registerEntity(this);
        currentActiveSprites = gunningRight;
    }

    public Bullet fire() {
        fireCooldow = 10;
        return new Bullet(this);
    }

    public Boolean canFire() {
        return fireCooldow == 0;
    }

    @Override
    public void update() {
        super.update();
        updateFireCooldown();
        updateCurrentSprite();
        updateCurrentSprites();
        if (!isCrouching()) {
            animator.updateAnimation(currentActiveSprites);
        }
        if (gamePad.isJumpPressed()) {
            super.startJump();
        }
    }

    @Override
    public void draw(Buffer buffer) {
        if (isCrouching()) {
            animator.drawCurrentAnimation(currentActiveSprite, buffer);
        } else {
            animator.drawCurrentAnimation(currentActiveSprites, buffer);
        }
        if (GameSettings.DEBUG_ENABLED) {
            drawHitBox(buffer);
        }
    }

    private void initClassContent() {
        initSpriteSheets();
        initSprites();
        initAnimator();
    }

    private void initAnimator() {
        this.animator = new Animator(this);
    }

    private void initSpriteSheets() {
        ImagesReader imagesReader = new ImagesReader();
        spriteSheet = imagesReader.readImage(SPRITE_PATH);
        this.spriteReader = new SpriteReader(spriteSheet);
    }

    private void initSprites() {
        gunningRight = new Image[2];
        runningRight = new Image[5];
        jumpingRight = new Image[4];
        gunningLeft = new Image[2];
        runningLeft = new Image[5];
        jumpingLeft = new Image[4];
        crouchRight = new BufferedImage(17, 34, TYPE_INT_RGB);
        crouchLeft = new BufferedImage(17, 34, TYPE_INT_RGB);
        deathSprite = new BufferedImage(11, 34, TYPE_INT_RGB);
        readSprites();
    }

    private void readSprites() {
        spriteReader.readRightSpriteSheet(gunningRight, PlayerSpritesheetInfo.GUNNING_RIGHT_FRAMES_START_X, PlayerSpritesheetInfo.GUNNING_RIGHT_FRAMES_START_Y, PlayerSpritesheetInfo.GUNNING_WIDTH, PlayerSpritesheetInfo.GUNNING_HEIGHT, gunningRight.length);
        spriteReader.readRightSpriteSheet(runningRight, PlayerSpritesheetInfo.RUNNING_RIGHT_FRAMES_START_X, PlayerSpritesheetInfo.RUNNING_RIGHT_FRAMES_START_Y, PlayerSpritesheetInfo.RUNNING_WIDTH, PlayerSpritesheetInfo.RUNNING_HEIGHT, runningRight.length);
        spriteReader.readRightSpriteSheet(jumpingRight, PlayerSpritesheetInfo.RIGHT_JUMPING_FRAMES_START_X, PlayerSpritesheetInfo.RIGHT_JUMPING_FRAMES_START_Y, PlayerSpritesheetInfo.JUMPING_WIDTH, PlayerSpritesheetInfo.JUMPING_HEIGHT, jumpingRight.length);

        spriteReader.readRightSpriteSheet(gunningLeft, PlayerSpritesheetInfo.GUNNING_LEFT_FRAMES_START_X, PlayerSpritesheetInfo.GUNNING_LEFT_FRAMES_START_Y, PlayerSpritesheetInfo.GUNNING_WIDTH, PlayerSpritesheetInfo.GUNNING_HEIGHT, gunningRight.length);
        spriteReader.readRightSpriteSheet(runningLeft, PlayerSpritesheetInfo.RUNNING_LEFT_FRAMES_START_X, PlayerSpritesheetInfo.RUNNING_LEFT_FRAMES_START_Y, PlayerSpritesheetInfo.RUNNING_WIDTH, PlayerSpritesheetInfo.RUNNING_HEIGHT, runningRight.length);
        spriteReader.readRightSpriteSheet(jumpingLeft, PlayerSpritesheetInfo.LEFT_JUMPING_FRAMES_START_X, PlayerSpritesheetInfo.LEFT_JUMPING_FRAMES_START_Y, PlayerSpritesheetInfo.JUMPING_WIDTH, PlayerSpritesheetInfo.JUMPING_HEIGHT, jumpingRight.length);

        crouchRight = spriteReader.readSingleFrame(PlayerSpritesheetInfo.RIGHT_CROUCH_FRAME_START_X, PlayerSpritesheetInfo.RIGHT_CROUCH_FRAME_START_Y, PlayerSpritesheetInfo.CROUCH_WIDTH, PlayerSpritesheetInfo.CROUCH_HEIGHT);
        crouchLeft = spriteReader.readSingleFrame(PlayerSpritesheetInfo.LEFT_CROUCH_FRAME_START_X, PlayerSpritesheetInfo.LEFT_CROUCH_FRAME_START_Y, PlayerSpritesheetInfo.CROUCH_WIDTH, PlayerSpritesheetInfo.CROUCH_HEIGHT);

        deathSprite = spriteReader.readSingleFrame(PlayerSpritesheetInfo.DEATH_FRAME_START_X, PlayerSpritesheetInfo.DEATH_FRAME_START_Y, PlayerSpritesheetInfo.DEATH_WIDTH, PlayerSpritesheetInfo.DEATH_HEIGHT);
    }

    private void updateFireCooldown() {
        fireCooldow--;
        if (fireCooldow <=0) {
            fireCooldow = 0;
        }
    }

    private void updateCurrentSprite() {
        if (isCrouching() && getDirection() == Direction.RIGHT) {
            currentActiveSprite = crouchRight;
        } else if (isCrouching() && getDirection() == Direction.RIGHT) {
            currentActiveSprite = crouchLeft;
        }
    }

    private void updateCurrentSprites() {
        if (getDirection() == Direction.LEFT) {
            currentActiveSprites = runningLeft;
        } else if (getDirection() == Direction.RIGHT) {
            currentActiveSprites = runningRight;
        } else if (jumping && getDirection() == Direction.RIGHT) {
            currentActiveSprites = jumpingRight;
        } else if (jumping && getDirection() == Direction.LEFT) {
            currentActiveSprites = jumpingLeft;
        }
    }

    public boolean isCrouching() {
        return getDirection() == Direction.DOWN && super.gravity == 1;
    }
}
