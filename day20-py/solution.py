import sys
import math
from dataclasses import dataclass
from datetime import datetime

datetime_start = datetime.now()

UNKNOWN="UNKNOWN"
CORNER="CORNER"
EDGE="EDGE"
MIDDLE="MIDDLE"

MONSTER=["                  # ","#    ##    ##    ###"," #  #  #  #  #  #   "]

@dataclass
class Tile:
    id: int
    rows: list
    cols: list
    edges: list
    # top_lr, btm_lr, lft_tb, rgt_tb, top_rl, btm_rl, lft_bt, rgt_bt
    # 0       1       2       3       4       5       6       7
    def flip_rot_edges(self, flip, rot):
        edges = self.edges
        e = edges.copy()
        if flip==1:
            e = [e[i] for i in [4,5,3,2,0,1,7,6]]
        # elif flip==2:
        #     e = [e[i] for i in [1,0,6,7,5,4,2,3]]
        while rot>0:
            rot-=1
            e = [e[i] for i in [6,7,1,0,2,3,5,4]]
        return e
    def flip_rot_trim(self, flip, rot):
        cols=self.cols.copy()
        rows=self.rows.copy()
        if flip==1:
            new_rows = []
            for row in rows:
                new_rows.append(row[::-1])
            rows = new_rows
            cols = cols[::-1]

        data = []
        if rot==0:
            data = rows
        elif rot==1:
            for col in cols:
                data.append(col[::-1])
        elif rot==2:
            for row in rows[::-1]:
                data.append(row[::-1])
        elif rot==3:
            for col in cols[::-1]:
                data.append(col)
        else:
            print(f"ERROR transforming Tile {self.id} by flip={flip} and rot={rot}")

        # trim
        img=[]
        for row in data[1:-1]:
            img_row=row[1:-1]
            img.append(img_row)
        return img


def elapsedTimeMs(since=datetime_start):
    return datetime.now()-since

def readTile(lines,i):
    idrow = lines[i]
    tile_id=int(idrow[5:-1])
    i+=1
    rows=[]
    while i<len(lines) and len(lines[i])>0:
        rows.append(lines[i])
        i+=1
    top_lr = rows[0]
    btm_lr = rows[-1]

    cols=[""]*len(rows)
    for y in range(len(rows)):
        row = rows[y]
        for x in range(len(row)):
            cols[x]+=row[x]
    lft_tb = cols[0]
    rgt_tb = cols[-1]
    top_rl, btm_rl, lft_bt, rgt_bt = [side[::-1] for side in [top_lr, btm_lr, lft_tb, rgt_tb]]
    edges = [top_lr, btm_lr, lft_tb, rgt_tb, top_rl, btm_rl, lft_bt, rgt_bt]
    return i,Tile(tile_id, rows, cols, edges)

def processLines(lines):
    tiles={}
    i=0
    while i<len(lines):
        i,tile=readTile(lines,i)
        tiles[tile.id]=tile
        i+=1
    return tiles

def readFile(filename = sys.argv[1]):
    filename = sys.argv[1]
    lines = []
    with open(filename) as f:
        lines = f.read().splitlines()
    return processLines(lines)

def printTileFully(tile,rot=0):
    print(tile.id,"rows")
    for row in tile.rows:
        print(' '.join(list(row)))
    print()

@dataclass
class Overlap:
    tile0: int
    tile1: int
    edge0: int
    edge1: int

def findOverlaps(tile0,tile1):
    e0 = tile0.edges
    e1 = tile1.edges
    overlaps=[]
    for i in range(len(e0)):
        for j in range(len(e1)):
            if e0[i] == e1[j]:
                overlaps.append(Overlap(tile0.id, tile1.id, i, j))
    return overlaps

def findMatchesAndSortByType(tiles):
    tileIds=list(tiles.keys())
    matchCount={}
    #record TODO side overlaps better, ...maybe {(tile_id0, e0) : (tile_id1, e1)}
    tileEdgeOverlaps={}
    for i in range(len(tileIds)-1):
        id0 = tileIds[i]
        for j in range(i+1, len(tileIds)):
            id1 = tileIds[j]
            overlaps = findOverlaps(tiles[id0],tiles[id1])
            for overlap in overlaps:
                tile0EdgeOverlaps = tileEdgeOverlaps.get(id0,[])
                tile0EdgeOverlaps.append(overlap)
                tileEdgeOverlaps[id0] = tile0EdgeOverlaps
                #add for other tile too
                tile1EdgeOverlaps = tileEdgeOverlaps.get(id1,[])
                tile1EdgeOverlaps.append(overlap)
                tileEdgeOverlaps[id1] = tile1EdgeOverlaps
            if len(overlaps) > 0:
                matchCount[id0] = matchCount.get(id0,0)+1
                matchCount[id1] = matchCount.get(id1,0)+1
    tilesByType={}
    for tile_id in tileIds:
        count = matchCount.get(tile_id,0)
        typ = {2:CORNER,3:EDGE,4:MIDDLE}.get(count, UNKNOWN)
        typList = tilesByType.get(typ,[])
        typList.append(tile_id)
        tilesByType[typ] = typList
    for typ in tilesByType:
        print(f"Found {len(tilesByType[typ])} with probable type {typ}")
    return tileEdgeOverlaps,tilesByType

def selectMatchingTile(left_tile_id, above_tile_id, tileEdgeOverlaps, tilesByType, placed, is_outer):
    if left_tile_id == None and above_tile_id == None:
        print("ERROR - both None")
        return None
    possible_tiles = set()
    if (is_outer):
        possible_tiles = set(tile_id for tile_id in tilesByType[EDGE]+tilesByType[CORNER] if tile_id not in placed)
    else:
        possible_tiles = set(tile_id for tile_id in tilesByType[MIDDLE] if tile_id not in placed)

    if left_tile_id != None and above_tile_id != None:
        left_ids = set([ol.tile0, ol.tile1][ol.tile0 == left_tile_id] for ol in tileEdgeOverlaps[left_tile_id])
        above_ids = set([ol.tile0, ol.tile1][ol.tile0 == above_tile_id] for ol in tileEdgeOverlaps[above_tile_id])
        for left_id in left_ids:
            if left_id in above_ids and left_id in possible_tiles:
                return left_id
        print(f"ERROR finding overlap from left {left_tile_id} and above {above_tile_id}")
    else:
        valid_tile_id = [left_tile_id, above_tile_id][left_tile_id == None]
        valid_ids = set([ol.tile0, ol.tile1][ol.tile0 == valid_tile_id] for ol in tileEdgeOverlaps[valid_tile_id])
        for valid_id in valid_ids:
            if valid_id in possible_tiles:
                return valid_id
        print(f"ERROR finding overlap from left {left_tile_id} and above {above_tile_id} from posibles: {possible_tiles} leading to valid ids: {valid_ids}")
    return None

def arrangeTiles(tiles):
    tileEdgeOverlaps,tilesByType = findMatchesAndSortByType(tiles)
    # defined as ALWAYS SQUARE
    # start with any corner, add left to right and go down, matching left and above
    sideLen = int(math.sqrt(len(tiles)))
    grid = []
    placed = set()
    for y in range(sideLen):
        row=[]
        for x in range(sideLen):
            selectedTile = None
            if x==y==0:
                selectedTile = tilesByType[CORNER][0]
            else:
                left_tile_id = None
                if x>0:
                    left_tile_id = row[x-1]
                above_tile_id = None
                if y>0:
                    above_tile_id = grid[y-1][x]
                is_outer = x in [0, sideLen-1] or y in [0,sideLen-1]
                selectedTile = selectMatchingTile(left_tile_id,above_tile_id,tileEdgeOverlaps,tilesByType,placed,is_outer)
            row.append(selectedTile)
            placed.add(selectedTile)
        grid.append(row)
    return grid, tileEdgeOverlaps

def alignFirst(tile,right_id,down_id,tileEdgeOverlaps):
    overlaps_right = [[o.edge0,o.edge1][o.tile0 == right_id] for o in tileEdgeOverlaps[tile.id] if right_id in [o.tile0, o.tile1]]
    overlaps_down = [[o.edge0,o.edge1][o.tile0 == down_id] for o in tileEdgeOverlaps[tile.id] if down_id in [o.tile0, o.tile1]]
    
    right_pat_allowed = [tile.edges[o] for o in overlaps_right]
    down_pat_allowed = [tile.edges[o] for o in overlaps_down]

    fliprot=None

    #[top_lr, btm_lr, lft_tb, rgt_tb, 
    for flip in range(2):
        for rot in range(4):
            edges = tile.flip_rot_edges(flip, rot)
            if edges[3] in right_pat_allowed and edges[1] in down_pat_allowed:
                fliprot = (flip,rot)
                return fliprot

    if fliprot==None:
        print("ERROR aligning first tile :(")
        exit()
    return fliprot

def alignToLeftOnly(tile, rhs_of_tile_to_left):
    #[top_lr, btm_lr, lft_tb, rgt_tb,
    for flip in range(2):
        for rot in range(4):
            edges = tile.flip_rot_edges(flip, rot)
            if edges[2] == rhs_of_tile_to_left:
                return flip,rot
    print(f"ERROR aligning top row tile {tile.id} :(")
    exit()

def alignToAboveOnly(tile,btm_of_tile_above):
    #[top_lr, btm_lr, lft_tb, rgt_tb,
    for flip in range(2):
        for rot in range(4):
            edges = tile.flip_rot_edges(flip, rot)
            if edges[0] == btm_of_tile_above:
                return flip,rot
    print(f"ERROR aligning top row tile {tile.id} :(")
    exit()

def alignTiles(tiles, grid, tileEdgeOverlaps):
    sideLen = int(math.sqrt(len(tiles)))
    xforms=[]
    for y in range(sideLen):
        xform_row = []
        for x in range(sideLen):
            tile_id = grid[y][x]
            tile = tiles[tile_id]
            fliprot=(0,0)
            if x==y==0:
                right = grid[y][x+1]
                down = grid[y+1][x]
                fliprot=alignFirst(tile,right,down,tileEdgeOverlaps)
                # print(f"transformed first tile: flip {flip} and rot {rot}")
            elif y==0:
                left = grid[y][x-1]
                left_flip, left_rot = xform_row[x-1]
                rhs_of_tile_to_left = tiles[left].flip_rot_edges(left_flip, left_rot)[3]
                #[top_lr, btm_lr, lft_tb, rgt_tb,
                fliprot=alignToLeftOnly(tile,rhs_of_tile_to_left)
            else:
                above_id = grid[y-1][x]
                above_flip, above_rot = xforms[y-1][x]
                btm_of_tile_above = tiles[above_id].flip_rot_edges(above_flip, above_rot)[1]
                fliprot=alignToAboveOnly(tile,btm_of_tile_above)
            xform_row.append(fliprot)
        print(" ".join(f"{r}" for r in xform_row))
        xforms.append(xform_row)

    tileLen = len(tiles[list(tiles.keys())[0]].rows)-2 # yuk = shout store this somewhere... "-2" due to edges getting dropped
    photo=[""]*sideLen*tileLen
    for ty in range(sideLen):
        for tx in range(sideLen):
            tile_id=grid[ty][tx]
            tile=tiles[tile_id]
            flip,rot=xforms[ty][tx]
            img_data=tile.flip_rot_trim(flip, rot)
            py=ty*tileLen
            for y in range(tileLen):
                photo[py+y]+=img_data[y]
    return photo

tiles = readFile()
print(f"{elapsedTimeMs()} read in {len(tiles)} tiles")
grid, tileEdgeOverlaps = arrangeTiles(tiles)
print(f"{elapsedTimeMs()} arranged tiles into grid (see below) and product of corner ids is {grid[0][0] * grid[-1][0] * grid[0][-1] * grid[-1][-1]}")
for row in grid:
    print(" ".join(str(i) for i in row))

########## visually testing flip and rot...
# tile_id=list(tiles.keys())[0]
# tile=tiles[tile_id]
# results = set()
# tileLen = len(tile.rows)
# SPACER = (" "*(tileLen-2))
# for f in range(2):
#     print("Tile",tile_id)
#     for row in tile.rows:
#         print("\t"+" ".join(list(row)))
#     print()
#     for r in range(4):
#         top_lr, btm_lr, lft_tb, rgt_tb = tile.flip_rot_edges(f, r)[:4]
#         result = "|".join([top_lr, btm_lr, lft_tb, rgt_tb])
#         print(f"flip={f}, rot={r} and seen={result in results}")
#         for y in range(tileLen):
#             if y==0:
#                 row = top_lr
#             elif y==tileLen-1:
#                 row = btm_lr
#             else:
#                 row = lft_tb[y]+SPACER+rgt_tb[y]
#             print("\t"+" ".join(list(row)))
#         print()
#         results.add(result)
########## RESULT: 8 distinct results :)

photo = alignTiles(tiles, grid, tileEdgeOverlaps)
print(f"{elapsedTimeMs()} aligned to form photo:")
# for row in photo:
#     print(" ".join(list(row)))
@dataclass(unsafe_hash=True)
class Point:
    x: int
    y: int

@dataclass
class PixelImage:
    height: int
    width: int
    pixels: set

def strToPixels(photo):
    pixels=set()
    photoHeight=len(photo)
    photoWidth=len(photo[0])
    for y in range(photoHeight):
        for x in range(photoWidth):
            if photo[y][x]=="#":
                pixels.add(Point(x,y))
    return PixelImage(photoHeight, photoWidth, pixels)

def flip_rot_pixels(img,flip,rot):
    if flip==1:
        new_pixels=set()
        for pixel in img.pixels:
            x=img.width - pixel.x - 1
            y=pixel.y
            new_pixels.add(Point(x,y))
        img = PixelImage(img.height, img.width, new_pixels)
    while rot>0:
        rot-=1
        new_pixels=set()
        for pixel in img.pixels:
            x=img.height - pixel.y - 1
            y=pixel.x
            new_pixels.add(Point(x,y))
        img = PixelImage(img.width, img.height, new_pixels)
    return img

def printImg(txt, img):
    print(txt)
    for y in range(img.height):
        row="\t"
        for x in range(img.width):
            if Point(x,y) in img.pixels:
                row+="#"
            else:
                row+="."
        print(row)
    print()

def monsterAt(x,y,searchImg,habitat):
    monsterPixels=set()
    for deltaPixel in searchImg.pixels:
        pixel = Point(deltaPixel.x+x, deltaPixel.y+y)
        if pixel not in habitat.pixels:
            return False, set()
        else:
            monsterPixels.add(pixel)
    return True, monsterPixels

def findMonsters(searchImg, habitat):
    origins=set()
    monsterPixels=set()
    for y in range(habitat.height): # is the +1 necessary?
        for x in range(habitat.width):
            found, pixels = monsterAt(x,y,searchImg,habitat)
            if found:
                origins.add(Point(x,y))
                monsterPixels.update(pixels)
    return origins, monsterPixels


def waterRoughNess(photo):
    habitat=strToPixels(photo)
    print(f"There are {len(habitat.pixels)} active pixels out of a total of {habitat.height * habitat.width} in the {habitat.height}x{habitat.width} photo")
    monster=strToPixels(MONSTER)
    print(f"There are {len(monster.pixels)} active pixels out of a total of {monster.height * monster.width} in the {monster.height}x{monster.width} MONSTER")

    for flip in range(2):
        for rot in range(4):
            searchImg=flip_rot_pixels(monster,flip,rot)
            ## visual testing ==>
            # printImg(f"monster with flip={flip} and rot={rot}:",searchImg)
            origins, monsterPixels = findMonsters(searchImg, habitat)
            if len(origins) > 0:
                roughness = len(habitat.pixels) - len(origins) * len(monster.pixels)
                print(f"{elapsedTimeMs()} found {len(origins)} MONSTERS in the habitat with a flip={flip} and rot={rot} giving a water roughness of {roughness}")

                #visual check --> ERROR!! flip/rot (1,3) lower left tile is wrong...
                for y in range(habitat.height):
                    row="\t"
                    for x in range(habitat.width):
                        pixel = Point(x,y)
                        if pixel in monsterPixels:
                            row+="O"
                        elif pixel in habitat.pixels:
                            row+="#"
                        else:
                            row+="."
                    print(" ".join(list(row)))
waterRoughNess(photo)
