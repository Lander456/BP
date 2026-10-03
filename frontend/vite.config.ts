import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/image': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/patient': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/map': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/sector': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
