// noinspection DuplicatedCode,JSUnresolvedVariable,JSUnresolvedFunction,JSCheckFunctionSignatures,JSCommentMatchesSignature,JSUnusedGlobalSymbols,JSUnusedLocalSymbols

const renderClass = "jp.ngt.rtm.render.RailPartsRenderer";
TONG_MOVE = 0.35;
TONG_POS = 1.0 / 7.0;
HALF_GAUGE = 0.5647;
/**レール長で割る*/
YAW_RATE = 450.0;

function isBRERails1() {}
function isBRERails2() {}
function isBRERails3() {}
function isBRERails4() {}
function isBRERails5() {}

function renderRailStatic(tileEntity, posX, posY, posZ, par8, pass) {
    if (isBRERails5()) {
        renderRailStatic2InBRERails5(tileEntity, posX, posY, posZ);
    } else {
        renderRailStatic2(tileEntity, posX, posY, posZ);
    }

    renderer.renderStaticParts(tileEntity, posX, posY, posZ);
}

function renderRailDynamic(tileEntity, posX, posY, posZ, par8, pass) {
    if (isBRERails4()) return;

    if (renderer.isSwitchRail(tileEntity)) {
        renderRailDynamic2(tileEntity, posX, posY, posZ);
    }
}

function shouldRenderObject(tileEntity, objName, len, pos) {
    if (isBRERails4()) return false;

    let ballastIndex;
    if (isBRERails1() || isBRERails2()) {
        ballastIndex = (pos % 14) + 1;
    } else if (isBRERails3()) {
        ballastIndex = (pos % 8) + 1;
    }

    const tag = "Ba" + ballastIndex;

    if (renderer.isSwitchRail(tileEntity)) {
        if (!isBRERails5() && objName === tag) return true;
        if ((isBRERails1() || isBRERails2() || isBRERails5()) && Pcs.containsName(objName)) return true;
        if (isBRERails3() && Base.containsName(objName)) return true;
    } else {
        if (
            fixtureL.containsName(objName) ||
            fixtureR.containsName(objName) ||
            leftParts.containsName(objName) ||
            rightParts.containsName(objName)
        ) {
            return true;
        }

        if (isBRERails5()) {
            const max = Math.floor(tileEntity.getRailMap(null).getLength() * 2.0);

            if (start.containsName(objName) && pos === 0) {
                return true;
            } else if (end.containsName(objName) && pos === max) {
                return true;
            }
            return false;
        }

        if (objName === tag || Pcs.containsName(objName)) return true;
    }

    return false;
}

function renderRailStatic2(tileEntity, x, y, z) {
    if (isBRERails1()) return;
    if (renderer.isSwitchRail(tileEntity)) return;

    const position = tileEntity.getRailPositions()[0];
    const railMap = tileEntity.getRailMap(null);
    const max = Math.floor(railMap.getLength() * 2.0);
    const originPos = railMap.getRailPos(max, 0);
    const originHeight = railMap.getRailHeight(max, 0);
    const originCantRad = railMap.getCant(max, 0) * (Math.PI / 180);
    const originCantHeight = 1.5 * Math.abs(Math.sin(originCantRad));

    renderer.bindTexture(
        renderer.getModelObject().textures[0].material.texture
    );

    GL11.glPushMatrix();
    GL11.glTranslatef(
        x + position.posX - position.blockX,
        y + position.posY - position.blockY - 0.0625,
        z + position.posZ - position.blockZ
    );

    for (let i = 0; i <= max; ++i) {
        const pos = railMap.getRailPos(max, i);

        const brightness = renderer.getBrightness(
            renderer.getWorld(tileEntity),
            pos[1],
            renderer.getY(tileEntity),
            pos[0]
        );
        renderer.setBrightness(brightness);

        GL11.glPushMatrix();
        GL11.glTranslatef(
            pos[1] - originPos[1],
            railMap.getRailHeight(max, i) - originHeight + originCantHeight,
            pos[0] - originPos[0]
        );
        GL11.glRotatef(railMap.getRailRotation(max, i), 0.0, 1.0, 0.0);
        GL11.glRotatef(railMap.getRailPitch(max, i) * -1, 1.0, 0.0, 0.0);
        if (isBRERails4()) GL11.glRotatef(railMap.getCant(max, i), 0.0, 0.0, 1.0);

        if (isBRERails2()) {
            let Wpos = i % 13;

            wallR[Wpos].render(renderer);
            wallL[Wpos].render(renderer);
            grooveR.render(renderer);
            grooveL.render(renderer);
            if (i === 0 || i === max) wallE.render(renderer);
        } else if (isBRERails3()) {
            let Wpos = i % 13;

            wallR[Wpos].render(renderer);
            wallL[Wpos].render(renderer);
            Base.render(renderer);
            grooveR.render(renderer);
            grooveL.render(renderer);
        } else if (isBRERails4()) {
            const yaw = railMap.getRailRotation(max, i);
            const yaw2 = (i === max ? yaw : railMap.getRailRotation(max, i + 1)) - yaw;

            if (yaw2 >= 0.005 && i % 10 === 1) {
                anchorR.render(renderer);
            }
            if (yaw2 <= -0.005 && i % 10 === 1) {
                anchorL.render(renderer);
            }
        }

        GL11.glPopMatrix();
    }
    GL11.glPopMatrix();
}

function renderRailStatic2InBRERails5(tileEntity, x, y, z) {
    if (renderer.isSwitchRail(tileEntity)) return;

    const position = tileEntity.getRailPositions()[0];
    const railMap = tileEntity.getRailMap(null);
    const railLength = railMap.getLength();
    const origPos = railMap.getRailPos(1, 0);
    const origHeight = railMap.getRailHeight(1, 0);
    const origCantRad = railMap.getCant(1, 0) * (Math.PI / 180);
    const origCantHeight = 1.5 * Math.abs(Math.sin(origCantRad));
    const numberOfSegment = Math.ceil(railLength / 10); //レール全体のセグメント分割数
    const numberOfCrosstieInSegment = Math.floor(railLength / numberOfSegment * 2.5); //セグメントの枕木分割数
    const numberOfCrosstie = numberOfCrosstieInSegment * numberOfSegment; //レール全体の枕木分割数

    renderer.bindTexture(
        renderer.getModelObject().textures[0].material.texture
    );

    GL11.glPushMatrix();
    GL11.glTranslatef(
        x + position.posX - position.blockX,
        y + position.posY - position.blockY - 0.0625,
        z + position.posZ - position.blockZ
    );

    for (let i = 0; i <= numberOfSegment - 1; ++i) {
        const p0 = railMap.getRailPos(numberOfSegment, i); //i番目のセグメントの始点
        const p1 = railMap.getRailPos(numberOfSegment, i + 1); //i番目のセグメントの終点
        const x0 = p1[1] - p0[1]; //i番目のセグメントの長さX成分
        const z0 = p1[0] - p0[0]; //i番目のセグメントの長さZ成分

        for (let j = 0; j <= numberOfCrosstieInSegment; ++j) {
            const crosstieOffset = i * numberOfCrosstieInSegment; //i番目のセグメントより手前にある枕木の数

            const brightness = renderer.getBrightness(
                renderer.getWorld(tileEntity),
                p1[1],
                renderer.getY(tileEntity),
                p1[0]
            );
            renderer.setBrightness(brightness);

            GL11.glPushMatrix();
            GL11.glTranslatef(
                p0[1] + (j / numberOfCrosstieInSegment) * x0 - origPos[1],
                railMap.getRailHeight(numberOfCrosstie, crosstieOffset + j) - origHeight + origCantHeight,
                p0[0] + (j / numberOfCrosstieInSegment) * z0 - origPos[0]
            );
            GL11.glRotatef((Math.atan(x0 / z0) * 180.0) / Math.PI, 0.0, 1.0, 0.0);
            GL11.glRotatef(railMap.getRailPitch(numberOfCrosstie, crosstieOffset + j) * -1, 1.0, 0.0, 0.0);
            GL11.glRotatef(railMap.getCant(numberOfCrosstie, crosstieOffset + j), 0.0, 0.0, 1.0);

            Pcs.render(renderer);

            GL11.glPopMatrix();
        }
    }
    GL11.glPopMatrix();
}

function renderRailDynamic2(tileEntity, x, y, z) {
    if (tileEntity.getSwitch() == null) return;

    const position = tileEntity.getRailPositions()[0];
    const points = tileEntity.getSwitch().getPoints();

    renderer.bindTexture(renderer.getModelObject().textures[0].material.texture);

    GL11.glPushMatrix();
    GL11.glTranslatef(
        x + position.posX - position.blockX,
        y,
        z + position.posZ - position.blockZ
    );

    for (let i = 0; i < points.length; ++i) {
        renderPoint(tileEntity, points[i]);
    }

    GL11.glPopMatrix();
}

function renderPoint(tileEntity, point) {
    if (point.branchDir === RailDir.NONE) {
        const railMap = point.rmMain;
        const max = Math.floor(railMap.getLength() * 2.0);
        const halfMax = Math.floor((max * 4) / 5);

        renderer.renderRailMapStatic(
            tileEntity,
            railMap,
            max,
            point.mainDirIsPositive ? 0 : halfMax,
            point.mainDirIsPositive ? halfMax : max,
            leftParts,
            rightParts,
            fixtureR,
            fixtureL
        );
    } else {
        const tongIndex = Math.floor(point.rmMain.getLength() * 2.0 * TONG_POS); //どの位置を末端モデルで描画

        renderRailMapDynamic(
            tileEntity,
            point.rmMain,
            point.branchDir,
            point.mainDirIsPositive,
            point.getMovement() * TONG_MOVE,
            tongIndex
        );

        renderRailMapDynamic(
            tileEntity,
            point.rmBranch,
            point.branchDir.invert(),
            point.branchDirIsPositive,
            (1.0 - point.getMovement()) * TONG_MOVE,
            tongIndex
        );
    }
}

/**
 * RailMapごとの描画
 * @param movement 開通時:0.0
 */
function renderRailMapDynamic(tileEntity, railMap, direction, isPositive, movement, tongIndex) {
    const railLength = railMap.getLength();
    const max = Math.floor(railLength * 2.0);
    const halfMax = Math.floor((max * 4) / 5);

    const startPos = tileEntity.getStartPoint();
    const revXZ = RailPosition.REVISION[tileEntity.getRailPositions()[0].direction];

    const moveX = -startPos[0] + 0.5 + revXZ[0];
    const moveZ = -startPos[2] + 0.5 + revXZ[1];
    const directionFixture = (isPositive && direction === RailDir.LEFT) || (!isPositive && direction === RailDir.RIGHT) ? -1.0 : 1.0;

    if (isBRERails2() || isBRERails3()) {
        for (let j = 0; j <= max; ++j) {
            const wallIndex = j % 13;
            const pos = railMap.getRailPos(max, j);

            const brightness = renderer.getBrightness(
                renderer.getWorld(tileEntity),
                pos[1],
                renderer.getY(tileEntity),
                pos[0]
            );
            renderer.setBrightness(brightness);

            GL11.glPushMatrix();
            GL11.glTranslatef(
                moveX + pos[1],
                0.0,
                moveZ + pos[0]
            );
            GL11.glRotatef(railMap.getRailRotation(max, j), 0.0, 1.0, 0.0);

            if (isBRERails3() && j === 0) joint.render(renderer);
            if ((isPositive && direction === RailDir.LEFT) || (!isPositive && direction === RailDir.RIGHT)) {
                wallR[wallIndex].render(renderer);
                grooveR.render(renderer);
            } else {
                wallL[wallIndex].render(renderer);
                grooveL.render(renderer);
            }

            GL11.glPopMatrix();
        }
    }

    for (let i = isPositive ? 0 : halfMax; i <= isPositive ? halfMax : max; ++i) {
        const pos = railMap.getRailPos(max, i);

        const brightness = renderer.getBrightness(
            renderer.getWorld(tileEntity),
            pos[1],
            renderer.getY(tileEntity),
            pos[0]
        );
        renderer.setBrightness(brightness);

        GL11.glPushMatrix();
        GL11.glTranslatef(
            moveX + pos[1],
            0.0,
            moveZ + pos[0]
        );
        GL11.glRotatef(railMap.getRailRotation(max, i), 0.0, 1.0, 0.0);

        if ((isPositive && direction === RailDir.LEFT) || (!isPositive && direction === RailDir.RIGHT)) {
            rightParts.render(renderer);
            fixtureR.render(renderer);
        } else {
            leftParts.render(renderer);
            fixtureL.render(renderer);
        }

        let separateRate = (1.0 - sigmoid2((isPositive ? i : max - i) / halfMax)) * movement * directionFixture;
        const halfGaugeMove = directionFixture * HALF_GAUGE;

        GL11.glTranslatef(separateRate - halfGaugeMove, 0.0, 0.0);
        GL11.glRotatef(((separateRate * YAW_RATE) / railLength) * (isPositive ? -1.0 : 1.0), 0.0, 1.0, 0.0);
        GL11.glTranslatef(halfGaugeMove, 0.0, 0.0);

        if (direction === RailDir.LEFT) {
            if (isPositive) {
                if (i === tongIndex) {
                    tongBL.render(renderer);
                } else if (i > tongIndex) {
                    leftParts.render(renderer);
                    fixtureL.render(renderer);
                }
            } else {
                if (i === max - tongIndex) {
                    tongFR.render(renderer);
                } else if (i < max - tongIndex) {
                    rightParts.render(renderer);
                    fixtureR.render(renderer);
                }
            }
        } else {
            if (isPositive) {
                if (i === tongIndex) {
                    tongBR.render(renderer);
                } else if (i > tongIndex) {
                    rightParts.render(renderer);
                    fixtureR.render(renderer);
                }
            } else {
                if (i === max - tongIndex) {
                    tongFL.render(renderer);
                } else if (i < max - tongIndex) {
                    leftParts.render(renderer);
                    fixtureL.render(renderer);
                }
            }
        }

        GL11.glPopMatrix();
    }
}

function sigmoid2(x) {
    const d0 = x * 3.5;
    return d0 / Math.sqrt(1.0 + d0 * d0) * 0.75 + 0.25;
}
