import { defineConfig, mergeConfig } from 'vitest/config'
import viteConfig from './vite.config'
import path from 'path'

export default mergeConfig(viteConfig, defineConfig({
    test: {
        globals: true,
        environment: 'happy-dom',
        alias: {
            '@': path.resolve(__dirname, './src'),
        }
    }
}))
