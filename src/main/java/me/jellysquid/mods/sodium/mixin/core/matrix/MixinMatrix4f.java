package me.jellysquid.mods.sodium.mixin.core.matrix;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import me.jellysquid.mods.sodium.client.util.math.Matrix4fExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public class MixinMatrix4f implements Matrix4fExtended {
    @Shadow
    protected float a00;

    @Shadow
    protected float a01;

    @Shadow
    protected float a02;

    @Shadow
    protected float a03;

    @Shadow
    protected float a10;

    @Shadow
    protected float a11;

    @Shadow
    protected float a12;

    @Shadow
    protected float a13;

    @Shadow
    protected float a20;

    @Shadow
    protected float a21;

    @Shadow
    protected float a22;

    @Shadow
    protected float a23;

    @Shadow
    protected float a30;

    @Shadow
    protected float a31;

    @Shadow
    protected float a32;

    @Shadow
    protected float a33;

    @Override
    public void translate(float x, float y, float z) {
        this.a03 = this.a00 * x + this.a01 * y + this.a02 * z + this.a03;
        this.a13 = this.a10 * x + this.a11 * y + this.a12 * z + this.a13;
        this.a23 = this.a20 * x + this.a21 * y + this.a22 * z + this.a23;
        this.a33 = this.a30 * x + this.a31 * y + this.a32 * z + this.a33;
    }

    @Override
    public float transformVecX(float x, float y, float z) {
        return (this.a00 * x) + (this.a01 * y) + (this.a02 * z) + (this.a03 * 1.0f);
    }

    @Override
    public float transformVecY(float x, float y, float z) {
        return (this.a10 * x) + (this.a11 * y) + (this.a12 * z) + (this.a13 * 1.0f);
    }

    @Override
    public float transformVecZ(float x, float y, float z) {
        return (this.a20 * x) + (this.a21 * y) + (this.a22 * z) + (this.a23 * 1.0f);
    }

    @Override
    public void rotate(Quaternion quaternion) {
        boolean x = quaternion.i() != 0.0F;
        boolean y = quaternion.j() != 0.0F;
        boolean z = quaternion.k() != 0.0F;

        // Try to determine if this is a simple rotation on one axis component only
        if (x) {
            if (!y && !z) {
                this.rotateX(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (y) {
            if (!z) {
                this.rotateY(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (z) {
            this.rotateZ(quaternion);
        }
    }

    private void rotateX(Quaternion quaternion) {
        float x = quaternion.i();
        float w = quaternion.r();

        float xx = 2.0F * x * x;
        float ta11 = 1.0F - xx;
        float ta22 = 1.0F - xx;

        float xw = x * w;

        float ta21 = 2.0F * xw;
        float ta12 = 2.0F * -xw;

        float a01 = this.a01 * ta11 + this.a02 * ta21;
        float a02 = this.a01 * ta12 + this.a02 * ta22;
        float a11 = this.a11 * ta11 + this.a12 * ta21;
        float a12 = this.a11 * ta12 + this.a12 * ta22;
        float a21 = this.a21 * ta11 + this.a22 * ta21;
        float a22 = this.a21 * ta12 + this.a22 * ta22;
        float a31 = this.a31 * ta11 + this.a32 * ta21;
        float a32 = this.a31 * ta12 + this.a32 * ta22;

        this.a01 = a01;
        this.a02 = a02;
        this.a11 = a11;
        this.a12 = a12;
        this.a21 = a21;
        this.a22 = a22;
        this.a31 = a31;
        this.a32 = a32;
    }

    private void rotateY(Quaternion quaternion) {
        float y = quaternion.j();
        float w = quaternion.r();

        float yy = 2.0F * y * y;
        float ta00 = 1.0F - yy;
        float ta22 = 1.0F - yy;
        float yw = y * w;
        float ta20 = 2.0F * -yw;
        float ta02 = 2.0F * yw;

        float a00 = this.a00 * ta00 + this.a02 * ta20;
        float a02 = this.a00 * ta02 + this.a02 * ta22;
        float a10 = this.a10 * ta00 + this.a12 * ta20;
        float a12 = this.a10 * ta02 + this.a12 * ta22;
        float a20 = this.a20 * ta00 + this.a22 * ta20;
        float a22 = this.a20 * ta02 + this.a22 * ta22;
        float a30 = this.a30 * ta00 + this.a32 * ta20;
        float a32 = this.a30 * ta02 + this.a32 * ta22;

        this.a00 = a00;
        this.a02 = a02;
        this.a10 = a10;
        this.a12 = a12;
        this.a20 = a20;
        this.a22 = a22;
        this.a30 = a30;
        this.a32 = a32;
    }

    private void rotateZ(Quaternion quaternion) {
        float z = quaternion.k();
        float w = quaternion.r();

        float zz = 2.0F * z * z;
        float ta00 = 1.0F - zz;
        float ta11 = 1.0F - zz;
        float zw = z * w;
        float ta10 = 2.0F * zw;
        float ta01 = 2.0F * -zw;

        float a00 = this.a00 * ta00 + this.a01 * ta10;
        float a01 = this.a00 * ta01 + this.a01 * ta11;
        float a10 = this.a10 * ta00 + this.a11 * ta10;
        float a11 = this.a10 * ta01 + this.a11 * ta11;
        float a20 = this.a20 * ta00 + this.a21 * ta10;
        float a21 = this.a20 * ta01 + this.a21 * ta11;
        float a30 = this.a30 * ta00 + this.a31 * ta10;
        float a31 = this.a30 * ta01 + this.a31 * ta11;

        this.a00 = a00;
        this.a01 = a01;
        this.a10 = a10;
        this.a11 = a11;
        this.a20 = a20;
        this.a21 = a21;
        this.a30 = a30;
        this.a31 = a31;
    }

    private void rotateXYZ(Quaternion quaternion) {
        float x = quaternion.i();
        float y = quaternion.j();
        float z = quaternion.k();
        float w = quaternion.r();

        float xx = 2.0F * x * x;
        float yy = 2.0F * y * y;
        float zz = 2.0F * z * z;
        float ta00 = 1.0F - yy - zz;
        float ta11 = 1.0F - zz - xx;
        float ta22 = 1.0F - xx - yy;
        float xy = x * y;
        float yz = y * z;
        float zx = z * x;
        float xw = x * w;
        float yw = y * w;
        float zw = z * w;
        float ta10 = 2.0F * (xy + zw);
        float ta01 = 2.0F * (xy - zw);
        float ta20 = 2.0F * (zx - yw);
        float ta02 = 2.0F * (zx + yw);
        float ta21 = 2.0F * (yz + xw);
        float ta12 = 2.0F * (yz - xw);

        float a00 = this.a00 * ta00 + this.a01 * ta10 + this.a02 * ta20;
        float a01 = this.a00 * ta01 + this.a01 * ta11 + this.a02 * ta21;
        float a02 = this.a00 * ta02 + this.a01 * ta12 + this.a02 * ta22;
        float a10 = this.a10 * ta00 + this.a11 * ta10 + this.a12 * ta20;
        float a11 = this.a10 * ta01 + this.a11 * ta11 + this.a12 * ta21;
        float a12 = this.a10 * ta02 + this.a11 * ta12 + this.a12 * ta22;
        float a20 = this.a20 * ta00 + this.a21 * ta10 + this.a22 * ta20;
        float a21 = this.a20 * ta01 + this.a21 * ta11 + this.a22 * ta21;
        float a22 = this.a20 * ta02 + this.a21 * ta12 + this.a22 * ta22;
        float a30 = this.a30 * ta00 + this.a31 * ta10 + this.a32 * ta20;
        float a31 = this.a30 * ta01 + this.a31 * ta11 + this.a32 * ta21;
        float a32 = this.a30 * ta02 + this.a31 * ta12 + this.a32 * ta22;

        this.a00 = a00;
        this.a01 = a01;
        this.a02 = a02;
        this.a10 = a10;
        this.a11 = a11;
        this.a12 = a12;
        this.a20 = a20;
        this.a21 = a21;
        this.a22 = a22;
        this.a30 = a30;
        this.a31 = a31;
        this.a32 = a32;
    }
}
