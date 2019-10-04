import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildServerStrike, defaultValue } from 'app/shared/model/bot/guild-server-strike.model';

export const ACTION_TYPES = {
  FETCH_GUILDSERVERSTRIKE_LIST: 'guildServerStrike/FETCH_GUILDSERVERSTRIKE_LIST',
  FETCH_GUILDSERVERSTRIKE: 'guildServerStrike/FETCH_GUILDSERVERSTRIKE',
  CREATE_GUILDSERVERSTRIKE: 'guildServerStrike/CREATE_GUILDSERVERSTRIKE',
  UPDATE_GUILDSERVERSTRIKE: 'guildServerStrike/UPDATE_GUILDSERVERSTRIKE',
  DELETE_GUILDSERVERSTRIKE: 'guildServerStrike/DELETE_GUILDSERVERSTRIKE',
  RESET: 'guildServerStrike/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildServerStrike>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GuildServerStrikeState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildServerStrikeState = initialState, action): GuildServerStrikeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDSERVERSTRIKE):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDSERVERSTRIKE):
    case REQUEST(ACTION_TYPES.DELETE_GUILDSERVERSTRIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE):
    case FAILURE(ACTION_TYPES.CREATE_GUILDSERVERSTRIKE):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDSERVERSTRIKE):
    case FAILURE(ACTION_TYPES.DELETE_GUILDSERVERSTRIKE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERSTRIKE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDSERVERSTRIKE):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDSERVERSTRIKE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDSERVERSTRIKE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-server-strikes';

// Actions

export const getEntities: ICrudGetAllAction<IGuildServerStrike> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVERSTRIKE_LIST,
    payload: axios.get<IGuildServerStrike>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGuildServerStrike> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVERSTRIKE,
    payload: axios.get<IGuildServerStrike>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildServerStrike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDSERVERSTRIKE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IGuildServerStrike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDSERVERSTRIKE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildServerStrike> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDSERVERSTRIKE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
