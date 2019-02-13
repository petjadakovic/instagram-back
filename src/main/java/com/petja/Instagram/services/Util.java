package com.petja.Instagram.services;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;


public class Util {
	public static BufferedImage resizeImage(Image image, int max_size) {
		double width = image.getWidth(null);
		double height = image.getHeight(null);

		if(width > height) {
			double image_ratio = height / width;
			System.out.println(image_ratio + " - ratio");
			width = max_size;
			height = max_size * image_ratio;
		} else {
			double image_ratio = width / height;
			width = max_size * image_ratio;
			height = max_size;
		}

		image = image.getScaledInstance((int)width,(int)height, Image.SCALE_DEFAULT);
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		return bimage;
	}
	public static BufferedImage resizeImageCircle(Image image, int max_size) {
		double width = image.getWidth(null);
		double height = image.getHeight(null);

		if(width > height) {
			double image_ratio = height / width;
			width = max_size;
			height = max_size * image_ratio;
		} else {
			double image_ratio = width / height;
			width = max_size * image_ratio;
			height = max_size;
		}

		image = image.getScaledInstance((int)width,(int)height, Image.SCALE_DEFAULT);
		
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		
		WritableRaster source = bimage.getRaster();
		
		WritableRaster target = rafgl.Util.createRaster(max_size, max_size, true);
		
		int rgba[] = new int[4];
		
		for(int y = 0; y < max_size; y++)
		{

			for(int x = 0; x < max_size; x++)
			{
				

				int srcX = x - (max_size - source.getWidth()) / 2;
				int srcY = y - (max_size - source.getHeight()) / 2;
				
				boolean circle = Math.sqrt(Math.abs(x - max_size / 2) * Math.abs(x - max_size / 2) + Math.abs(y - max_size / 2) * Math.abs(y - max_size / 2)) < max_size / 2; 				
				if(srcX >= 0 && srcX < source.getWidth() && srcY >= 0 && srcY < source.getHeight() && circle) {
					source.getPixel(srcX, srcY, rgba);
				} else {
					rgba = new int[] {0,0,0,0};
				}
				target.setPixel(x, y, rgba);
			}
		}

		
		return rafgl.Util.rasterToImage(target);
	}
}
