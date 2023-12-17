// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.glass;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.struct.Struct;
import edu.wpi.first.wpilibj.util.Color8Bit;
import java.nio.ByteBuffer;

class CanvasText2d {
  public final float m_x;
  public final float m_y;
  public final float m_fontSize;
  public final float m_wrapWidth;
  public final char[] m_text;
  public final Color8Bit m_color;
  public final int m_opacity;
  public final int m_zOrder;

  /**
   * Constructs a CanvasText2d.
   *
   * @param x The x coordinate of the text
   * @param y The y coordinate of the text
   * @param fontSize The size of the text
   * @param wrapWidth The width at which the text should wrap. 0 for no wrap
   * @param text The text. The max of length the text is 64 characters
   * @param color The color of the text
   * @param opacity The opacity of the text [0-255]
   * @param zOrder The z-order of the text
   */
  public CanvasText2d(
      float x,
      float y,
      float fontSize,
      float wrapWidth,
      String text,
      Color8Bit color,
      int opacity,
      int zOrder) {
    this(x, y, fontSize, wrapWidth, text.toCharArray(), color, opacity, zOrder);
  }

  /**
   * Constructs a CanvasText2d.
   *
   * @param x The x coordinate of the text
   * @param y The y coordinate of the text
   * @param size The size of the text
   * @param wrapWidth The width at which the text should wrap
   * @param text The text. The max of length the text is 64 characters
   * @param color The color of the text
   * @param opacity The opacity of the text [0-255]
   * @param zOrder The z-order of the text
   */
  public CanvasText2d(
      float x,
      float y,
      float size,
      float wrapWidth,
      char[] text,
      Color8Bit color,
      int opacity,
      int zOrder) {
    // TODO: check text length
    m_x = x;
    m_y = y;
    m_fontSize = size;
    m_wrapWidth = wrapWidth;
    m_text = text;
    m_color = color;
    m_opacity = MathUtil.clamp(opacity, 0, 255);
    m_zOrder = zOrder;
  }

  public static class AStruct implements Struct<CanvasText2d> {
    @Override
    public Class<CanvasText2d> getTypeClass() {
      return CanvasText2d.class;
    }

    @Override
    public String getTypeString() {
      return "struct:CanvasText2d";
    }

    @Override
    public int getSize() {
      return kSizeFloat * 4 + kSizeInt8 * 64 + kSizeInt8 * 4 + kSizeInt32;
    }

    @Override
    public String getSchema() {
      return "float x;float y;float fontSize;float wrapWidth;char text[64];uint8 color[4];int32 zOrder";
    }

    @Override
    public CanvasText2d unpack(ByteBuffer bb) {
      float x = bb.getFloat();
      float y = bb.getFloat();
      float fontSize = bb.getFloat();
      float wrapWidth = bb.getFloat();
      char[] text = new char[64];
      for (int i = 0; i < 64; i++) {
        text[i] = (char) bb.get();
      }
      Color8Bit color = new Color8Bit(bb.get(), bb.get(), bb.get());
      int opacity = bb.get();
      int zOrder = bb.getInt();

      return new CanvasText2d(x, y, fontSize, wrapWidth, text, color, opacity, zOrder);
    }

    @Override
    public void pack(ByteBuffer bb, CanvasText2d value) {
      System.out.println(bb.position());
      bb.putFloat(value.m_x);
      bb.putFloat(value.m_y);
      bb.putFloat(value.m_fontSize);
      bb.putFloat(value.m_wrapWidth);
      for (int i = 0; i < value.m_text.length; i++) {
        bb.put((byte) value.m_text[i]);
      }
      for (int i = value.m_text.length; i < 64; i++) {
        bb.put((byte) 0);
      }
      bb.put((byte) value.m_color.red);
      bb.put((byte) value.m_color.green);
      bb.put((byte) value.m_color.blue);
      bb.put((byte) value.m_opacity);
      bb.putInt(value.m_zOrder);
      System.out.println(bb.position());
    }
  }

  public static final AStruct struct = new AStruct();
}
