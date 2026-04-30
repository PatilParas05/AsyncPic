package com.paraspatil.compose

object AsyncPicShaders {

    const val DISSOLVE_SHADER = """
        uniform shader content;
        uniform float progress;
        
        float random (vec2 uv){
            return fract(sin(dot(uv.xy, vec2(12.9898, 78.233))) * 43758.5453123);
        }
        
        vec4 main (vec2 coords){
            vec4 color = content.eval(coords);
            float noise = random(coords * 0.01);
            if(noise > progress) return vec4(0.0);
            return color * progress; 
        }
    """
    
    const val PIXELATE_SHADER = """
        uniform shader content;
        uniform float progress;
        uniform float2 resolution;
        
        vec4 main(vec2 coords){
            if (progress >= 1.0) return content.eval(coords);
            
            float blockSize = 50.0 * (1.0 - progress);
            
            if(blockSize < 1.0) return content.eval(coords);
            
            vec2 p = floor(coords / blockSize) * blockSize;
            return content.eval(p);
        }
    """

    const val WIPE_SHADER = """
        uniform shader content;
        uniform float progress;
        uniform float2 resolution;
        
        vec4 main(vec2 coords){
            // Wipe from left to right
            if (coords.x > progress * resolution.x) return vec4(0.0);
            return content.eval(coords);
        }
    """
}
