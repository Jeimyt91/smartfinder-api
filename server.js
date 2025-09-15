<<<<<<< HEAD
// server.js
import fs from 'fs';
import path from 'path';
import express from 'express';
import cors from 'cors';

const app = express();
const PORT = process.env.PORT || 3001;
const DB_PATH = path.resolve('db.json');

// ===== Utils persistencia =====
function readDB(){
  if(!fs.existsSync(DB_PATH)){
    const seed = {
      brands: [
        { id: crypto.randomUUID(), name: 'Apple' },
        { id: crypto.randomUUID(), name: 'Samsung' },
        { id: crypto.randomUUID(), name: 'Lenovo' },
        { id: crypto.randomUUID(), name: 'Xiaomi' },
      ],
      devices: [],
      comments: []
    };
    // Vincular devices con las brands seed:
    const [apple, samsung, lenovo, xiaomi] = seed.brands;
    seed.devices = [
      {
        id: crypto.randomUUID(),
        name: 'iPhone 15 Pro',
        brandId: apple.id,
        type: 'móvil',
        releaseDate: '2023-09-22',
        price: 5999900,
        shortDesc: 'Titanio, chip A17 Pro, cámaras Pro.',
        review: 'Tope de gama con excelente rendimiento y cámaras.',
        images: ['https://images.unsplash.com/photo-1695048133506-26f3856fc0d9?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.1" OLED 120Hz','Procesador':'A17 Pro','RAM':'8 GB','Almacenamiento':'256 GB','Batería':'3274 mAh','SO':'iOS' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Galaxy S24 Ultra',
        brandId: samsung.id,
        type: 'móvil',
        releaseDate: '2024-01-31',
        price: 6999900,
        shortDesc: 'Cámara 200 MP, S Pen, funciones IA.',
        review: 'Pantalla excelente y zoom; S Pen aporta valor.',
        images: ['https://images.unsplash.com/photo-1706868791334-1e05c770d34a?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.8" AMOLED 120Hz','Procesador':'Snapdragon 8 Gen 3','RAM':'12 GB','Almacenamiento':'256 GB','Batería':'5000 mAh','SO':'Android' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Lenovo Yoga Slim 7',
        brandId: lenovo.id,
        type: 'portátil',
        releaseDate: '2023-05-15',
        price: 4299900,
        shortDesc: 'Ultraligero, gran batería y buen teclado.',
        review: 'Equilibrio entre portabilidad y rendimiento.',
        images: ['https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'14" IPS','Procesador':'Ryzen 7','RAM':'16 GB','Almacenamiento':'512 GB SSD','SO':'Windows 11' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Xiaomi 14',
        brandId: xiaomi.id,
        type: 'móvil',
        releaseDate: '2024-02-26',
        price: 3499900,
        shortDesc: 'Cámaras Leica y gran rendimiento.',
        review: 'Relación calidad-precio destacada.',
        images: ['https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.36" AMOLED 120Hz','Procesador':'Snapdragon 8 Gen 3','RAM':'12 GB','Almacenamiento':'256 GB','Batería':'4610 mAh','SO':'Android' }
      },
    ];
    fs.writeFileSync(DB_PATH, JSON.stringify(seed, null, 2));
  }
  return JSON.parse(fs.readFileSync(DB_PATH, 'utf8'));
}
function writeDB(db){ fs.writeFileSync(DB_PATH, JSON.stringify(db, null, 2)); }

// ===== Middlewares =====
app.use(cors());
app.use(express.json());

// Auth muy simple (solo demo)
const DEMO_USER = { email: 'admin@smartfinder.local', password: 'admin123' };
const ACTIVE_TOKENS = new Set();

app.post('/api/login', (req, res) => {
  const { email, password } = req.body || {};
  if (email === DEMO_USER.email && password === DEMO_USER.password) {
    const token = crypto.randomUUID();
    ACTIVE_TOKENS.add(token);
    return res.json({ token, email });
  }
  res.status(401).json({ error: 'Credenciales inválidas' });
});

function requireAuth(req, res, next){
  const auth = req.headers.authorization || '';
  const token = auth.replace('Bearer ', '');
  if (ACTIVE_TOKENS.has(token)) return next();
  return res.status(401).json({ error: 'No autorizado' });
}

// ===== Brands =====
app.get('/api/brands', (req, res) => {
  const db = readDB();
  res.json(db.brands);
});
app.post('/api/brands', requireAuth, (req, res) => {
  const db = readDB();
  const name = (req.body?.name || '').trim();
  if(!name) return res.status(400).json({ error: 'Nombre requerido' });
  if (db.brands.some(b => b.name.toLowerCase() === name.toLowerCase()))
    return res.status(409).json({ error: 'La marca ya existe' });
  const brand = { id: crypto.randomUUID(), name };
  db.brands.push(brand); writeDB(db);
  res.status(201).json(brand);
});

// ===== Devices =====
app.get('/api/devices', (req, res) => {
  const db = readDB();
  let { q='', brandId='', type='', sort='release_desc' } = req.query;
  q = (q||'').toString().trim().toLowerCase();
  let data = db.devices.map(d => ({
    ...d,
    brandName: db.brands.find(b => b.id === d.brandId)?.name || '—'
  }));
  data = data.filter(d => {
    const hitText = !q || [d.name, d.brandName, d.shortDesc].some(v => (v||'').toLowerCase().includes(q));
    const hitBrand = !brandId || d.brandId === brandId;
    const hitType = !type || d.type === type;
    return hitText && hitBrand && hitType;
  });
  if (sort === 'release_asc') data.sort((a,b)=> new Date(a.releaseDate)-new Date(b.releaseDate));
  else if (sort === 'price_asc') data.sort((a,b)=> (a.price||0)-(b.price||0));
  else if (sort === 'price_desc') data.sort((a,b)=> (b.price||0)-(a.price||0));
  else data.sort((a,b)=> new Date(b.releaseDate)-new Date(a.releaseDate));
  res.json(data);
});

app.get('/api/devices/:id', (req, res) => {
  const db = readDB();
  const d = db.devices.find(x => x.id === req.params.id);
  if(!d) return res.status(404).json({ error: 'No encontrado' });
  res.json({ ...d, brandName: db.brands.find(b => b.id === d.brandId)?.name || '—' });
});

app.post('/api/devices', requireAuth, (req, res) => {
  const db = readDB();
  const payload = req.body || {};
  const dev = {
    id: crypto.randomUUID(),
    name: payload.name?.trim(),
    brandId: payload.brandId,
    type: payload.type,
    releaseDate: payload.releaseDate,
    price: Number(payload.price)||null,
    shortDesc: payload.shortDesc?.trim() || '',
    review: payload.review?.trim() || '',
    images: (payload.images || []).filter(Boolean),
    specs: payload.specs || {}
  };
  if(!dev.name || !dev.brandId || !dev.type || !dev.releaseDate)
    return res.status(400).json({ error: 'Campos obligatorios: name, brandId, type, releaseDate' });
  db.devices.push(dev); writeDB(db);
  res.status(201).json(dev);
});

app.put('/api/devices/:id', requireAuth, (req, res) => {
  const db = readDB();
  const i = db.devices.findIndex(x => x.id === req.params.id);
  if (i === -1) return res.status(404).json({ error: 'No encontrado' });
  db.devices[i] = { ...db.devices[i], ...req.body };
  writeDB(db);
  res.json(db.devices[i]);
});

app.delete('/api/devices/:id', requireAuth, (req, res) => {
  const db = readDB();
  const before = db.devices.length;
  db.devices = db.devices.filter(x => x.id !== req.params.id);
  writeDB(db);
  if (db.devices.length === before) return res.status(404).json({ error: 'No encontrado' });
  res.status(204).end();
});

// ===== Comments =====
app.get('/api/comments', (req, res) => {
  const db = readDB();
  const deviceId = (req.query.deviceId || '').toString();
  let list = db.comments;
  if (deviceId) list = list.filter(c => c.deviceId === deviceId);
  list.sort((a,b)=> new Date(b.createdAt)-new Date(a.createdAt));
  res.json(list);
});
app.post('/api/comments', (req, res) => {
  const db = readDB();
  const { deviceId, author, rating, text } = req.body || {};
  if(!deviceId || !text || String(text).trim().length < 5)
    return res.status(400).json({ error: 'deviceId y comentario (>=5 chars) requeridos' });
  const c = {
    id: crypto.randomUUID(),
    deviceId,
    author: (author||'Anónimo').trim(),
    rating: Number(rating)||5,
    text: String(text).trim(),
    createdAt: new Date().toISOString()
  };
  db.comments.push(c); writeDB(db);
  res.status(201).json(c);
});

app.listen(PORT, () => console.log(`API lista en http://localhost:${PORT}`));
=======
// server.js
import fs from 'fs';
import path from 'path';
import express from 'express';
import cors from 'cors';

const app = express();
const PORT = process.env.PORT || 3001;
const DB_PATH = path.resolve('db.json');

// ===== Utils persistencia =====
function readDB(){
  if(!fs.existsSync(DB_PATH)){
    const seed = {
      brands: [
        { id: crypto.randomUUID(), name: 'Apple' },
        { id: crypto.randomUUID(), name: 'Samsung' },
        { id: crypto.randomUUID(), name: 'Lenovo' },
        { id: crypto.randomUUID(), name: 'Xiaomi' },
      ],
      devices: [],
      comments: []
    };
    // Vincular devices con las brands seed:
    const [apple, samsung, lenovo, xiaomi] = seed.brands;
    seed.devices = [
      {
        id: crypto.randomUUID(),
        name: 'iPhone 15 Pro',
        brandId: apple.id,
        type: 'móvil',
        releaseDate: '2023-09-22',
        price: 5999900,
        shortDesc: 'Titanio, chip A17 Pro, cámaras Pro.',
        review: 'Tope de gama con excelente rendimiento y cámaras.',
        images: ['https://images.unsplash.com/photo-1695048133506-26f3856fc0d9?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.1" OLED 120Hz','Procesador':'A17 Pro','RAM':'8 GB','Almacenamiento':'256 GB','Batería':'3274 mAh','SO':'iOS' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Galaxy S24 Ultra',
        brandId: samsung.id,
        type: 'móvil',
        releaseDate: '2024-01-31',
        price: 6999900,
        shortDesc: 'Cámara 200 MP, S Pen, funciones IA.',
        review: 'Pantalla excelente y zoom; S Pen aporta valor.',
        images: ['https://images.unsplash.com/photo-1706868791334-1e05c770d34a?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.8" AMOLED 120Hz','Procesador':'Snapdragon 8 Gen 3','RAM':'12 GB','Almacenamiento':'256 GB','Batería':'5000 mAh','SO':'Android' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Lenovo Yoga Slim 7',
        brandId: lenovo.id,
        type: 'portátil',
        releaseDate: '2023-05-15',
        price: 4299900,
        shortDesc: 'Ultraligero, gran batería y buen teclado.',
        review: 'Equilibrio entre portabilidad y rendimiento.',
        images: ['https://images.unsplash.com/photo-1527443154391-507e9dc6c5cc?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'14" IPS','Procesador':'Ryzen 7','RAM':'16 GB','Almacenamiento':'512 GB SSD','SO':'Windows 11' }
      },
      {
        id: crypto.randomUUID(),
        name: 'Xiaomi 14',
        brandId: xiaomi.id,
        type: 'móvil',
        releaseDate: '2024-02-26',
        price: 3499900,
        shortDesc: 'Cámaras Leica y gran rendimiento.',
        review: 'Relación calidad-precio destacada.',
        images: ['https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?q=80&w=1470&auto=format&fit=crop'],
        specs: { 'Pantalla':'6.36" AMOLED 120Hz','Procesador':'Snapdragon 8 Gen 3','RAM':'12 GB','Almacenamiento':'256 GB','Batería':'4610 mAh','SO':'Android' }
      },
    ];
    fs.writeFileSync(DB_PATH, JSON.stringify(seed, null, 2));
  }
  return JSON.parse(fs.readFileSync(DB_PATH, 'utf8'));
}
function writeDB(db){ fs.writeFileSync(DB_PATH, JSON.stringify(db, null, 2)); }

// ===== Middlewares =====
app.use(cors());
app.use(express.json());

// Auth muy simple (solo demo)
const DEMO_USER = { email: 'admin@smartfinder.local', password: 'admin123' };
const ACTIVE_TOKENS = new Set();

app.post('/api/login', (req, res) => {
  const { email, password } = req.body || {};
  if (email === DEMO_USER.email && password === DEMO_USER.password) {
    const token = crypto.randomUUID();
    ACTIVE_TOKENS.add(token);
    return res.json({ token, email });
  }
  res.status(401).json({ error: 'Credenciales inválidas' });
});

function requireAuth(req, res, next){
  const auth = req.headers.authorization || '';
  const token = auth.replace('Bearer ', '');
  if (ACTIVE_TOKENS.has(token)) return next();
  return res.status(401).json({ error: 'No autorizado' });
}

// ===== Brands =====
app.get('/api/brands', (req, res) => {
  const db = readDB();
  res.json(db.brands);
});
app.post('/api/brands', requireAuth, (req, res) => {
  const db = readDB();
  const name = (req.body?.name || '').trim();
  if(!name) return res.status(400).json({ error: 'Nombre requerido' });
  if (db.brands.some(b => b.name.toLowerCase() === name.toLowerCase()))
    return res.status(409).json({ error: 'La marca ya existe' });
  const brand = { id: crypto.randomUUID(), name };
  db.brands.push(brand); writeDB(db);
  res.status(201).json(brand);
});

// ===== Devices =====
app.get('/api/devices', (req, res) => {
  const db = readDB();
  let { q='', brandId='', type='', sort='release_desc' } = req.query;
  q = (q||'').toString().trim().toLowerCase();
  let data = db.devices.map(d => ({
    ...d,
    brandName: db.brands.find(b => b.id === d.brandId)?.name || '—'
  }));
  data = data.filter(d => {
    const hitText = !q || [d.name, d.brandName, d.shortDesc].some(v => (v||'').toLowerCase().includes(q));
    const hitBrand = !brandId || d.brandId === brandId;
    const hitType = !type || d.type === type;
    return hitText && hitBrand && hitType;
  });
  if (sort === 'release_asc') data.sort((a,b)=> new Date(a.releaseDate)-new Date(b.releaseDate));
  else if (sort === 'price_asc') data.sort((a,b)=> (a.price||0)-(b.price||0));
  else if (sort === 'price_desc') data.sort((a,b)=> (b.price||0)-(a.price||0));
  else data.sort((a,b)=> new Date(b.releaseDate)-new Date(a.releaseDate));
  res.json(data);
});

app.get('/api/devices/:id', (req, res) => {
  const db = readDB();
  const d = db.devices.find(x => x.id === req.params.id);
  if(!d) return res.status(404).json({ error: 'No encontrado' });
  res.json({ ...d, brandName: db.brands.find(b => b.id === d.brandId)?.name || '—' });
});

app.post('/api/devices', requireAuth, (req, res) => {
  const db = readDB();
  const payload = req.body || {};
  const dev = {
    id: crypto.randomUUID(),
    name: payload.name?.trim(),
    brandId: payload.brandId,
    type: payload.type,
    releaseDate: payload.releaseDate,
    price: Number(payload.price)||null,
    shortDesc: payload.shortDesc?.trim() || '',
    review: payload.review?.trim() || '',
    images: (payload.images || []).filter(Boolean),
    specs: payload.specs || {}
  };
  if(!dev.name || !dev.brandId || !dev.type || !dev.releaseDate)
    return res.status(400).json({ error: 'Campos obligatorios: name, brandId, type, releaseDate' });
  db.devices.push(dev); writeDB(db);
  res.status(201).json(dev);
});

app.put('/api/devices/:id', requireAuth, (req, res) => {
  const db = readDB();
  const i = db.devices.findIndex(x => x.id === req.params.id);
  if (i === -1) return res.status(404).json({ error: 'No encontrado' });
  db.devices[i] = { ...db.devices[i], ...req.body };
  writeDB(db);
  res.json(db.devices[i]);
});

app.delete('/api/devices/:id', requireAuth, (req, res) => {
  const db = readDB();
  const before = db.devices.length;
  db.devices = db.devices.filter(x => x.id !== req.params.id);
  writeDB(db);
  if (db.devices.length === before) return res.status(404).json({ error: 'No encontrado' });
  res.status(204).end();
});

// ===== Comments =====
app.get('/api/comments', (req, res) => {
  const db = readDB();
  const deviceId = (req.query.deviceId || '').toString();
  let list = db.comments;
  if (deviceId) list = list.filter(c => c.deviceId === deviceId);
  list.sort((a,b)=> new Date(b.createdAt)-new Date(a.createdAt));
  res.json(list);
});
app.post('/api/comments', (req, res) => {
  const db = readDB();
  const { deviceId, author, rating, text } = req.body || {};
  if(!deviceId || !text || String(text).trim().length < 5)
    return res.status(400).json({ error: 'deviceId y comentario (>=5 chars) requeridos' });
  const c = {
    id: crypto.randomUUID(),
    deviceId,
    author: (author||'Anónimo').trim(),
    rating: Number(rating)||5,
    text: String(text).trim(),
    createdAt: new Date().toISOString()
  };
  db.comments.push(c); writeDB(db);
  res.status(201).json(c);
});

app.listen(PORT, () => console.log(`API lista en http://localhost:${PORT}`));
>>>>>>> ed4c0760bbd010e364d368aa6798b528a4817354
