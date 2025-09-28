// server.js  — servidor estático + proxy al backend
import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import { createProxyMiddleware } from 'http-proxy-middleware';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const app = express();
const PORT = process.env.PORT || 5500;

// Servir el frontend (carpeta /frontend)
app.use(express.static(path.join(__dirname, 'frontend')));

// Proxy para evitar CORS: /api -> http://localhost:8081
app.use('/api', createProxyMiddleware({
  target: 'http://localhost:8081',
  changeOrigin: true,
}));

// (Opcional) fallback si es SPA
app.get('*', (_req, res) => {
  res.sendFile(path.join(__dirname, 'frontend', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Front servido en http://localhost:${PORT}`);
  console.log(`Proxy /api -> http://localhost:8081`);
});
