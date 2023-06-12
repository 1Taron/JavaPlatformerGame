package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.MagicConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class JUR_Skull extends Enemy {

	private BufferedImage magiccastImgs[], magicclowImgs[];
	private int mgcastTick, mgclowTick;
	private int mgcastAni, mgclowAni;
	private boolean isAttack;

	private int attackboxX = 380;
	private int attackboxY = 380;
	private int posX = 120;
	private int posY = 40;


	public JUR_Skull(float x, float y) {
		super(x, y, JUR_SKULL_WIDTH, JUR_SKULL_HEIGHT, JUR_SKULL);
		loadImgs();
		initHitbox_Skull(120, 200);
		initAttackBox_Skull(attackboxX, attackboxY, posX, posY);
		isAttack = false;
	}

	private void loadImgs() {
		BufferedImage mgCast = LoadSave.GetSpriteAtlas(LoadSave.JUR_MAGIC_CAST);
		magiccastImgs = new BufferedImage[40];

		for (int i = 0; i < magiccastImgs.length; i++)
			magiccastImgs[i] = mgCast.getSubimage(i * 96, 0, 96, 96);

		BufferedImage mgClow = LoadSave.GetSpriteAtlas(LoadSave.JUR_MAGIC_CLOW);
		magicclowImgs = new BufferedImage[31];

		for (int i = 0; i < magicclowImgs.length; i++)
			magicclowImgs[i] = mgClow.getSubimage(i * 96, 0, 96, 96);

	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick_Skull();
		updateAttackBox();
		if (isAttack == true) {
			updateCastAni();
			updateClowAni();
			checkPlayerHit_Skull(attackBox, playing.getPlayer());
		}
	}

	private void updateClowAni() {
		mgclowTick++;
		if (mgclowTick >= 35) {
			mgclowTick = 0;
			mgclowAni++;
			if (mgclowAni >= 31)
				mgclowAni = 0;
		}
	}

	private void updateCastAni() {
		mgcastTick++;
		if (mgcastTick >= 35) {
			mgcastTick = 0;
			mgcastAni++;
			if (mgcastAni >= 40)
				mgcastAni = 0;
		}
	}

	public void draw(Graphics g, Playing playing) {
		if (isAttack == true)
			drawMagicbox(g, playing.getPlayer());
	}

	private void drawMagicbox(Graphics g, Player player) {

		g.drawImage(magiccastImgs[mgcastAni], (int) (player.x * Game.SCALE), (int) (player.y * Game.SCALE), attackboxX,
				attackboxY, null);
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {

		j_TileYcheck();
		switch (state) {
		case IDLE:
			isAttack = false;
			newState(RUNNING);
			break;
		case RUNNING:
			if (canSeePlayer_Skull(lvlData, playing.getPlayer())) {
				if (isPlayerCloseForAttack(playing.getPlayer())) 
					isAttack = false;
					
				newState(ATTACK);
			}
			break;
		case ATTACK:
			if (aniIndex == 0)
				attackChecked = false;
			if (aniIndex == 8 && !attackChecked) {
				isAttack = true;
			}
			break;
		case HIT:
			updatePushBackDrawOffset();
			break;
		}
	}

}
