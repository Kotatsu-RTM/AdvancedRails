// noinspection JSUnresolvedFunction,JSCommentMatchesSignature,JSUnresolvedVariable,JSCheckFunctionSignatures,DuplicatedCode,JSUnusedLocalSymbols

const renderClass = "jp.ngt.rtm.render.RailPartsRenderer";
importPackage(Packages.org.lwjgl.opengl);
importPackage(Packages.jp.ngt.rtm.render);
importPackage(Packages.jp.ngt.rtm.rail.util);

TONG_MOVE = 0.35;
TONG_POS = 1.0 / 7.0;
HALF_GAUGE = 0.5647;
/**レール長で割る*/
YAW_RATE = 450.0;

function init(par1, par2) {
}

function renderRailStatic(tileEntity, posX, posY, posZ, par8, pass) {
  renderRailStatic2(tileEntity, posX, posY, posZ);
  renderer.renderStaticParts(tileEntity, posX, posY, posZ);
}

function renderRailDynamic(tileEntity, posX, posY, posZ, par8, pass) {
  if (renderer.isSwitchRail(tileEntity)) {
    renderRailDynamic2(tileEntity, posX, posY, posZ);
  }
}

function shouldRenderObject(tileEntity, objName, len, pos) {
  let BallastTag = "Ba" + (pos % 14) + 1;

  if (renderer.isSwitchRail(tileEntity)) {
    return !!(objName === BallastTag || Pcs.containsName(objName));
  } else {
    return !!(objName === BallastTag ||
        Pcs.containsName(objName) ||
        fixtureL.containsName(objName) ||
        fixtureR.containsName(objName) ||
        leftParts.containsName(objName) ||
        rightParts.containsName(objName));
  }
}

function renderRailStatic2(tileEntity, par2, par4, par6) {
  if (renderer.isSwitchRail(tileEntity)) {
    return 0;
  } else {
    GL11.glPushMatrix();

    const rp = tileEntity.getRailPositions()[0];
    GL11.glTranslatef(
        par2 + (rp.posX - rp.blockX),
        par4 + (rp.posY - rp.blockY) - 0.0625,
        par6 + (rp.posZ - rp.blockZ)
    );

    renderer.bindTexture(
      renderer.getModelObject().textures[0].material.texture
    );

    const rm2 = tileEntity.getRailMap(null);
    const railLength = rm2.getLength();
    const max = Math.floor(railLength * 2.0);
    const origPos = rm2.getRailPos(max, 0);
    const origHeight = rm2.getRailHeight(max, 0);
    const origCant = rm2.getCant(max, 0);
    const origCantRad = origCant * (Math.PI / 180);
    const origCantHeight = 1.5 * Math.abs(Math.sin(origCantRad));

    for (let i = 0; i <= max; ++i) {
      const p1 = rm2.getRailPos(max, i);
      const brightness = renderer.getBrightness(
          renderer.getWorld(tileEntity),
          p1[1],
          renderer.getY(tileEntity),
          p1[0]
      );

      GL11.glPushMatrix();
      GL11.glTranslatef(
          p1[1] - origPos[1],
          rm2.getRailHeight(max, i) - origHeight + origCantHeight,
          p1[0] - origPos[0]
      );
      GL11.glRotatef(rm2.getRailRotation(max, i), 0.0, 1.0, 0.0);
      GL11.glRotatef(rm2.getRailPitch(max, i) * -1, 1.0, 0.0, 0.0);
      GL11.glRotatef(rm2.getCant(max, i), 0.0, 0.0, 1.0);
      renderer.setBrightness(brightness);

      GL11.glPopMatrix();
    }
    GL11.glPopMatrix();
  }
}

function renderRailDynamic2(tileEntity, par2, par4, par6) {
  if (tileEntity.getSwitch() == null) return;

  GL11.glPushMatrix();
  const rp = tileEntity.getRailPositions()[0];
  GL11.glTranslatef(
      par2 + (rp.posX - rp.blockX),
      par4,
      par6 + (rp.posZ - rp.blockZ)
  );

  renderer.bindTexture(renderer.getModelObject().textures[0].material.texture);

  //分岐レールの各頂点-中間点までを描画
  const pArray = tileEntity.getSwitch().getPoints();
  for (let i = 0; i < pArray.length; ++i) {
    renderPoint(tileEntity, pArray[i]);
  }

  GL11.glPopMatrix();
}

function renderPoint(tileEntity, point) {
  if (point.branchDir === RailDir.NONE) {
    //分岐なし部分
    const rm = point.rmMain;
    const max = Math.floor(rm.getLength() * 2.0);
    const halfMax = Math.floor((max * 4) / 5);
    const startIndex = point.mainDirIsPositive ? 0 : halfMax;
    const endIndex = point.mainDirIsPositive ? halfMax : max;
    renderer.renderRailMapStatic(
      tileEntity,
      rm,
      max,
      startIndex,
      endIndex,
      leftParts,
      rightParts,
      fixtureR,
      fixtureL
    );
  } else {
    const tongIndex = Math.floor(point.rmMain.getLength() * 2.0 * TONG_POS); //どの位置を末端モデルで描画
    let move = point.getMovement() * TONG_MOVE;
    renderRailMapDynamic(
      tileEntity,
      point.rmMain,
      point.branchDir,
      point.mainDirIsPositive,
      move,
      tongIndex
    );

    move = (1.0 - point.getMovement()) * TONG_MOVE;
    renderRailMapDynamic(
      tileEntity,
      point.rmBranch,
      point.branchDir.invert(),
      point.branchDirIsPositive,
      move,
      tongIndex
    );
  }
}

/**
 * RailMapごとの描画
 * @param move 開通時:0.0
 */
function renderRailMapDynamic(tileEntity, rms, dir, par3, move, tongIndex) {
  const railLength = rms.getLength();
  const max = Math.floor(railLength * 2.0);
  const halfMax = Math.floor((max * 4) / 5);
  const startIndex = par3 ? 0 : halfMax;
  const endIndex = par3 ? halfMax : max;

  const origPos = rms.getRailPos(max, 0);
  const startPos = tileEntity.getStartPoint();
  const revXZ = RailPosition.REVISION[tileEntity.getRailPositions()[0].direction];
  //当RailMapのレール全体の始点に対する移動差分
  const coreX = startPos[0] + 0.5 + revXZ[0];
  const coreZ = startPos[2] + 0.5 + revXZ[1];
  const moveX = origPos[1] - coreX;
  const moveZ = origPos[0] - coreZ;
  //向きによって移動量を反転させる
  const dirFixture =
      (par3 && dir === RailDir.LEFT) || (!par3 && dir === RailDir.RIGHT)
          ? -1.0
          : 1.0;

  //頂点-中間点
  for (let i = startIndex; i <= endIndex; ++i) {
    const p1 = rms.getRailPos(max, i);
    const brightness = renderer.getBrightness(
        renderer.getWorld(tileEntity),
        p1[1],
        renderer.getY(tileEntity),
        p1[0]
    );

    GL11.glPushMatrix();
    GL11.glTranslatef(
        moveX + (p1[1] - origPos[1]),
        0.0,
        moveZ + (p1[0] - origPos[0])
    );
    GL11.glRotatef(rms.getRailRotation(max, i), 0.0, 1.0, 0.0);
    renderer.setBrightness(brightness);

    //分岐してない側のレール
    //開始位置が逆の場合は左右反対側のパーツを描画
    if ((par3 && dir === RailDir.LEFT) || (!par3 && dir === RailDir.RIGHT)) {
      rightParts.render(renderer);
      fixtureR.render(renderer);
    } else {
      leftParts.render(renderer);
      fixtureL.render(renderer);
    }

    //トング部分の離れ度合い(0.0-1.0)
    let separateRate = (par3 ? i : max - i) / halfMax;
    separateRate = (1.0 - sigmoid2(separateRate)) * move * dirFixture;
    const halfGaugeMove = dirFixture * HALF_GAUGE;
    GL11.glTranslatef(separateRate - halfGaugeMove, 0.0, 0.0);
    const yaw2 = ((separateRate * YAW_RATE) / railLength) * (par3 ? -1.0 : 1.0);
    GL11.glRotatef(yaw2, 0.0, 1.0, 0.0);
    GL11.glTranslatef(halfGaugeMove, 0.0, 0.0);

    //分岐してる側のレール
    if (dir === RailDir.LEFT) {
      if (par3) {
        //始点を共有
        if (i === tongIndex) {
          tongBL.render(renderer); //トングレール
        } else if (i > tongIndex) {
          leftParts.render(renderer); //リードレール
          fixtureL.render(renderer);
        }
      } //終点を共有
      else {
        if (i === max - tongIndex) {
          tongFR.render(renderer); //トングレール
        } else if (i < max - tongIndex) {
          rightParts.render(renderer); //リードレール
          fixtureR.render(renderer);
        }
      }
    } //dir == RailDir.RIGHT
    else {
      if (par3) {
        //始点を共有
        if (i === tongIndex) {
          tongBR.render(renderer); //トングレール
        } else if (i > tongIndex) {
          rightParts.render(renderer); //リードレール
          fixtureR.render(renderer);
        }
      } //終点を共有
      else {
        if (i === max - tongIndex) {
          tongFL.render(renderer); //トングレール
        } else if (i < max - tongIndex) {
          leftParts.render(renderer); //リードレール
          fixtureL.render(renderer);
        }
      }
    }

    GL11.glPopMatrix();
  }
}

function sigmoid2(x) {
  const d0 = x * 3.5;
  const d1 = d0 / Math.sqrt(1.0 + d0 * d0); //シグモイド関数
  return d1 * 0.75 + 0.25;
}
