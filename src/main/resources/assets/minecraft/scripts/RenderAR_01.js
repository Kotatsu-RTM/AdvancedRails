// noinspection JSUnresolvedFunction,JSUnresolvedVariable,DuplicatedCode,JSUnusedLocalSymbols,JSCheckFunctionSignatures

const renderClass = "jp.ngt.rtm.render.RailPartsRenderer";
importPackage(Packages.org.lwjgl.opengl);
importPackage(Packages.jp.ngt.rtm.render);
importPackage(Packages.jp.ngt.rtm.rail.util);
importPackage(Packages.jp.ngt.ngtlib.util);
importPackage(Packages.jp.ngt.rtm);
let currentRailIndexField;

function init(par1, par2) {
}

function renderRailStatic(tileEntity, posX, posY, posZ, par8, pass) {
  renderRailStatic2(tileEntity, posX, posY, posZ, this.getCurrentRailIndex());
}

function renderRailDynamic(tileEntity, posX, posY, posZ, par8, pass) {
}

function shouldRenderObject(tileEntity, objName, len, pos) {
  return false;
}

function renderRailStatic2(tileEntity, par2, par4, par6, index) {
  if (renderer.isSwitchRail(tileEntity)) return;
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
  const max = Math.floor(railLength * 2);
  const origPos = rm2.getRailPos(max, 0);
  const origHeight = rm2.getRailHeight(max, 0);
  const origCant = rm2.getCant(max, 0);
  const origCantRad = origCant * (Math.PI / 180);
  const origCantHeight = 1.5 * Math.abs(Math.sin(origCantRad));

  for (let i = 0; i <= max; ++i) {
    const cant = rm2.getCant(max, i);
    const cantRad = cant * (Math.PI / 180);
    const cantHeight = 1.5 * Math.abs(Math.sin(cantRad));

    const p1 = rm2.getRailPos(max, i);

    const brightness = renderer.getBrightness(
        renderer.getWorld(tileEntity),
        p1[1],
        renderer.getY(tileEntity),
        p1[0]
    );
    renderer.setBrightness(brightness);
    GL11.glPushMatrix();
    GL11.glTranslatef(
        p1[1] - origPos[1],
        rm2.getRailHeight(max, i) - origHeight + origCantHeight - cantHeight,
        p1[0] - origPos[0]
    );
    GL11.glRotatef(rm2.getRailRotation(max, i), 0.0, 1.0, 0.0);
    GL11.glRotatef(rm2.getRailPitch(max, i) * -1, 1.0, 0.0, 0.0);
    if (this.isRightRail(tileEntity, index)) {
      GL11.glRotatef(180, 0.0, 1.0, 0.0);
    }
    if (index !== 0) {
      GL11.glTranslatef(2.0, 1.0, 0.0);
    }
    pf_01.render(renderer);
    GL11.glPopMatrix();
  }
  GL11.glPopMatrix();
}

function isLegacy() {
  return RTMCore.VERSION.indexOf("1.7.10") > -1;
}

function getCurrentRailIndex() {
  if (currentRailIndexField === undefined) {
    currentRailIndexField = (
      isLegacy() ? RailPartsRenderer.class : RailPartsRendererBase.class
    ).getDeclaredField("currentRailIndex");
    currentRailIndexField.setAccessible(true);
  }
  return currentRailIndexField.get(renderer);
}

function isRightRail(tileEntity, index) {
  if (index === 0) {
    return this.getBaseRailName(tileEntity).contains("PF_R");
  } else {
    return this.getRailName(tileEntity.subRails.get(index - 1)).contains("PF_R");
  }
}

function getBaseRailName(tileEntity) {
  return this.isLegacy()
    ? tileEntity.getProperty().railModel
    : tileEntity.getResourceState().getResourceName();
}

function getRailName(rs) {
  return this.isLegacy() ? rs.railModel : rs.getResourceName();
}
